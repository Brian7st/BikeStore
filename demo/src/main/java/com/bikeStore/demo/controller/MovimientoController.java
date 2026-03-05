package com.bikeStore.demo.controller;

import com.bikeStore.demo.documentacionSwagger.IMovimientoSwagger;
import com.bikeStore.demo.dto.request.MovimientoDtoResquest;
import com.bikeStore.demo.dto.response.MovimientoDtoResponse;
import com.bikeStore.demo.service.IMovimientoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/movimientos")
@RequiredArgsConstructor
public class MovimientoController implements IMovimientoSwagger {

    private final IMovimientoService movimientoService;

    @Override
    @PostMapping
    public ResponseEntity<MovimientoDtoResponse> registrar(@Valid @RequestBody MovimientoDtoResquest dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(movimientoService.registrar(dto));
    }

    @Override
    @GetMapping
    public ResponseEntity<List<MovimientoDtoResponse>> listarTodos() {
        return ResponseEntity.ok(movimientoService.listarTodos());
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<MovimientoDtoResponse> buscarPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(movimientoService.buscarPorId(id));
    }

}
