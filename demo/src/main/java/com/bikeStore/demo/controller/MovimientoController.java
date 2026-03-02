package com.bikeStore.demo.controller;

import com.bikeStore.demo.Entity.Movimiento;
import com.bikeStore.demo.dto.request.MovimientoDtoResquest;
import com.bikeStore.demo.dto.response.MovimientoDtoResponse;
import com.bikeStore.demo.service.MovimientoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movimientos")
@RequiredArgsConstructor
public class MovimientoController {

    private final MovimientoService movimientoService;

    // POST /api/movimientos  → registrar entrada o salida
    @PostMapping
    public ResponseEntity<MovimientoDtoResponse> registrar(@Valid @RequestBody MovimientoDtoResquest dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(movimientoService.registrar(dto));
    }

    // GET /api/movimiento
    @GetMapping
    public ResponseEntity<List<MovimientoDtoResponse>> ListarTodos(){
        return ResponseEntity.ok(movimientoService.listarTodos());
    }

    // GET /api/movimientos/{id}
    @GetMapping("/{id}")
    public ResponseEntity<MovimientoDtoResponse> buscarPorId(@PathVariable Integer id){
        return ResponseEntity.ok(movimientoService.buscarPorId(id));
    }

}
