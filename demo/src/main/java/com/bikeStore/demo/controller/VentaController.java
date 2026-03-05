package com.bikeStore.demo.controller;

import com.bikeStore.demo.documentacionSwagger.IVentaSwagger;
import com.bikeStore.demo.dto.request.VentaDtoRequest;
import com.bikeStore.demo.dto.response.VentaDtoResponse;
import com.bikeStore.demo.service.IVentaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/ventas")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class VentaController implements IVentaSwagger{
    private final IVentaService service;

    @Override
    public ResponseEntity<VentaDtoResponse> registrarVenta(VentaDtoResponse response) {
        return null;
    }

    @Override
    public ResponseEntity<VentaDtoResponse> registrarVenta() {
        return null;
    }

    @PostMapping
    @Override
    public ResponseEntity<VentaDtoResponse> registrarVenta(@RequestBody VentaDtoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.registrarVenta(request));
    }
}
