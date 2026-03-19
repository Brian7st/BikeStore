package com.bikeStore.demo.controller;

import com.bikeStore.demo.Entity.Usuario;
import com.bikeStore.demo.config.JwtService;

import com.bikeStore.demo.dto.response.LoginResponseDTO;
import com.bikeStore.demo.mapper.UsuarioMapper;
import com.bikeStore.demo.repository.UsuarioRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;
    private final UsuarioMapper usuarioMapper;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody com.bikeStore.demo.dto.request.LoginRequest request) {

        Usuario usuario = usuarioRepository.findByUserName(request.userName())
                .orElseThrow(() -> new RuntimeException("Credenciales incorrectas"));


        boolean passwordValida = passwordEncoder.matches(request.password(), usuario.getPassword());

        if (!usuario.isActivo() || !passwordValida) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String token = jwtService.generateToken(
                usuario.getUserName(),
                usuario.getRol().getNombre()
        );

        LoginResponseDTO response = usuarioMapper.toAuthResponse(usuario, token);
        return ResponseEntity.ok(response);
    }
}