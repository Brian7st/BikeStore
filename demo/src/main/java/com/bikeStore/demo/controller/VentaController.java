package com.bikeStore.demo.controller;

import com.bikeStore.demo.documentacionSwagger.IVentaSwagger;
import com.bikeStore.demo.dto.request.VentaDtoRequest;
import com.bikeStore.demo.dto.response.VentaDtoResponse;
import com.bikeStore.demo.service.IVentaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/ventas")
@RequiredArgsConstructor
public class VentaController implements IVentaSwagger {
    private final IVentaService service;

    @Override
    @PostMapping
    public ResponseEntity<VentaDtoResponse> registrarVenta(@Valid @RequestBody VentaDtoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.registrarVenta(request));
    }

    @Override
    @GetMapping
    public ResponseEntity<List<VentaDtoResponse>> listarTodas() {
        return ResponseEntity.ok(service.listarTodas());
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<VentaDtoResponse> buscarPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @Override
    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<VentaDtoResponse>> listarPorUsuario(@PathVariable UUID idUsuario) {
        return ResponseEntity.ok(service.listarPorUsuario(idUsuario));
    }
}
