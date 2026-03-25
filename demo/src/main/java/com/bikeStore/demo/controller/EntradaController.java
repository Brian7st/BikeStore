package com.bikeStore.demo.controller;

import com.bikeStore.demo.documentacionSwagger.IEntradaSwagger;
import com.bikeStore.demo.dto.request.EntradaDtoRequest;
import com.bikeStore.demo.dto.response.EntradaDtoResponse;
import com.bikeStore.demo.service.IEntradaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/entradas")
@RequiredArgsConstructor
public class EntradaController implements IEntradaSwagger {

    private final IEntradaService entradaService;

    @Override
    @PostMapping
    public ResponseEntity<List<EntradaDtoResponse>> registrar(@Valid @RequestBody EntradaDtoRequest dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(entradaService.registrarEntrada(dto));
    }

    @Override
    @GetMapping
    public ResponseEntity<List<EntradaDtoResponse>> listarTodas() {
        return ResponseEntity.ok(entradaService.listarTodas());
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<EntradaDtoResponse> buscarPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(entradaService.buscarPorId(id));
    }

    @Override
    @GetMapping("/proveedor/{idProveedor}")
    public ResponseEntity<List<EntradaDtoResponse>> listarPorProveedor(@PathVariable UUID idProveedor) {
        return ResponseEntity.ok(entradaService.listarPorProveedor(idProveedor));
    }
}
