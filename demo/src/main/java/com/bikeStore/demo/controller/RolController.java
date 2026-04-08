package com.bikeStore.demo.controller;

import com.bikeStore.demo.documentacionSwagger.IRolSwagger;
import com.bikeStore.demo.dto.request.RolDtoRequest;
import com.bikeStore.demo.dto.response.RolDtoResponse;
import com.bikeStore.demo.service.IRolService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class RolController implements IRolSwagger {

    private final IRolService rolService;

    @Override
    @PostMapping
    public ResponseEntity<RolDtoResponse> crear(@Valid @RequestBody RolDtoRequest dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(rolService.crear(dto));
    }

    @Override
    @GetMapping
    public ResponseEntity<List<RolDtoResponse>> listarTodos() {
        return ResponseEntity.ok(rolService.listarTodos());
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<RolDtoResponse> buscarPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(rolService.buscarPorId(id));
    }

    @Override
    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<RolDtoResponse> buscarPorNombre(@PathVariable String nombre) {
        return ResponseEntity.ok(rolService.buscarPorNombre(nombre));
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<RolDtoResponse> actualizar(
            @PathVariable UUID id,
            @Valid @RequestBody RolDtoRequest dto) {
        return ResponseEntity.ok(rolService.actualizar(id, dto));
    }

    @Override
    @PatchMapping("/{id}/activar")
    public ResponseEntity<RolDtoResponse> activar(@PathVariable UUID id) {
        return ResponseEntity.ok(rolService.cambiarEstado(id, true));
    }

    @Override
    @PatchMapping("/{id}/desactivar")
    public ResponseEntity<RolDtoResponse> desactivar(@PathVariable UUID id) {
        return ResponseEntity.ok(rolService.cambiarEstado(id, false));
    }
}