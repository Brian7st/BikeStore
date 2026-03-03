package com.bikeStore.demo.controller;

import com.bikeStore.demo.documentacionSwagger.IDetalleVentaSwagger;
import com.bikeStore.demo.dto.response.DetalleVentaDtoResponse;
import com.bikeStore.demo.service.IDetalleVentaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/detalles-venta")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DetalleVentaController implements IDetalleVentaSwagger{
    private final IDetalleVentaService service;

    @Override
    @GetMapping
    public ResponseEntity<List<DetalleVentaDtoResponse>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<DetalleVentaDtoResponse> buscarPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @Override
    @GetMapping("/venta/{idVenta}")
    public ResponseEntity<List<DetalleVentaDtoResponse>> listarPorVenta(@PathVariable UUID idVenta) {
        return ResponseEntity.ok(service.listarPorVenta(idVenta));
    }
}
