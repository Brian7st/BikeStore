package com.bikeStore.demo.controller;

import com.bikeStore.demo.documentacionSwagger.ISalidaSwagger;
import com.bikeStore.demo.dto.request.SalidaBajaDtoRequest;
import com.bikeStore.demo.dto.response.SalidaDtoResponse;
import com.bikeStore.demo.service.ISalidaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/salidas")
@RequiredArgsConstructor
public class SalidaController implements ISalidaSwagger {

    private final ISalidaService salidaService;

    @Override
    @PostMapping("/baja")
    public ResponseEntity<SalidaDtoResponse> registrarBaja(@Valid @RequestBody SalidaBajaDtoRequest dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(salidaService.registrarBaja(dto));
    }

    @Override
    @GetMapping
    public ResponseEntity<List<SalidaDtoResponse>> listarTodas() {
        return ResponseEntity.ok(salidaService.listarTodas());
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<SalidaDtoResponse> buscarPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(salidaService.buscarPorId(id));
    }

    @Override
    @GetMapping("/venta/{idVenta}")
    public ResponseEntity<List<SalidaDtoResponse>> listarPorVenta(@PathVariable UUID idVenta) {
        return ResponseEntity.ok(salidaService.listarPorVenta(idVenta));
    }
}
