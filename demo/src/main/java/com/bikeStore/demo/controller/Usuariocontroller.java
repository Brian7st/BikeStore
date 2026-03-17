package com.bikeStore.demo.controller;


import com.bikeStore.demo.documentacionSwagger.IUsuarioSwagger;
import com.bikeStore.demo.dto.request.UsuarioDtoRequest;
import com.bikeStore.demo.dto.response.UsuarioDtoResponse;
import com.bikeStore.demo.service.IUsuarioService;
import com.bikeStore.demo.service.impl.UsuarioServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class Usuariocontroller implements IUsuarioSwagger {

    private final IUsuarioService usuarioService;

    @Override
    @PostMapping("/registro")
    public ResponseEntity<UsuarioDtoResponse> crear(@Valid @RequestBody UsuarioDtoRequest dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.registrarUsuario(dto));
    }
    @Override
    @GetMapping
    public ResponseEntity<List<UsuarioDtoResponse>> listarTodos() {
        return ResponseEntity.ok(usuarioService.listarTodos());
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDtoResponse> buscarPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(usuarioService.buscarPorId(id));
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDtoResponse> actualizar(
            @PathVariable UUID id,
            @Valid @RequestBody UsuarioDtoRequest dto) {
        return ResponseEntity.ok(usuarioService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable UUID id) {
        usuarioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
    @Override
    @PatchMapping("/{id}/activar")
    public ResponseEntity<UsuarioDtoResponse> activar(@PathVariable UUID id) {
        return ResponseEntity.ok(usuarioService.cambiarEstado(id, true));
    }
    @Override
    @PatchMapping("/{id}/desactivar")
    public ResponseEntity<UsuarioDtoResponse> desactivar(@PathVariable UUID id) {
        return ResponseEntity.ok(usuarioService.cambiarEstado(id, false));
    }
}
