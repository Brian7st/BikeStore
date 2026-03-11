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
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;
    private final UsuarioMapper usuarioMapper;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody com.bikeStore.demo.dto.request.LoginRequest request) {


        Usuario usuario = usuarioRepository.findByUsername(request.userName())
                .orElseThrow(() -> new RuntimeException("Credenciales incorrectas"));


        if (!usuario.isActivo() || !usuario.getPassword().equals(request.password())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }


        String token = jwtService.generateToken(
                usuario.getIdUsuario(),
                usuario.getRol().getIdRol(),
                usuario.getUserName(),
                usuario.getRol().getNombre()
        );


        LoginResponseDTO response = usuarioMapper.toAuthResponse(usuario, token);

        return ResponseEntity.ok(response);
    }
}