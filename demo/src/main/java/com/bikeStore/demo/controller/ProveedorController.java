package com.bikeStore.demo.controller;

import com.bikeStore.demo.documentacionSwagger.IProveedorSwagger;
import com.bikeStore.demo.dto.request.ProveedorDtoRequest;
import com.bikeStore.demo.dto.response.ProveedorDtoResponse;
import com.bikeStore.demo.service.IProveedorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/proveedores")
@RequiredArgsConstructor
public class ProveedorController implements IProveedorSwagger {

    private final IProveedorService proveedorService;

    @Override
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProveedorDtoResponse> crear(@Valid @RequestBody ProveedorDtoRequest dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(proveedorService.crear(dto));
    }

    @Override
    @GetMapping
    public ResponseEntity<List<ProveedorDtoResponse>> listarTodos() {
        return ResponseEntity.ok(proveedorService.listarTodos());
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<ProveedorDtoResponse> buscarPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(proveedorService.buscarPorId(id));
    }

    @Override
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProveedorDtoResponse> actualizar(
            @PathVariable UUID id,
            @Valid @RequestBody ProveedorDtoRequest dto) {
        return ResponseEntity.ok(proveedorService.actualizar(id, dto));
    }

    @Override
    @PatchMapping("/{id}/activar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProveedorDtoResponse> activar(@PathVariable UUID id) {
        return ResponseEntity.ok(proveedorService.cambiarEstado(id, true));
    }

    @Override
    @PatchMapping("/{id}/desactivar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProveedorDtoResponse> desactivar(@PathVariable UUID id) {
        return ResponseEntity.ok(proveedorService.cambiarEstado(id, false));
    }
}
