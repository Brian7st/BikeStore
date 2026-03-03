package com.bikeStore.demo.controller;

import com.bikeStore.demo.documentacionSwagger.IBicicletaSwagger;
import com.bikeStore.demo.dto.request.BicicletaDtoRequest;
import com.bikeStore.demo.dto.request.BicicletaUpdateDto;
import com.bikeStore.demo.dto.response.BicicletaDtoResponse;

import com.bikeStore.demo.service.IBicicletaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/bicicletas")
@RequiredArgsConstructor
public class BicicletaController implements IBicicletaSwagger {

    private final IBicicletaService service;


    @Override
    @PostMapping
    public ResponseEntity<BicicletaDtoResponse> crearBicicleta(@RequestBody BicicletaDtoRequest dtoRequest){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crearBicicleta(dtoRequest));
    }

    @Override
    @GetMapping
    public ResponseEntity<List<BicicletaDtoResponse>> listarTodoBicicleta(){
        return ResponseEntity.ok(service.listarTodo());
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<BicicletaDtoResponse> obtenerPorIdBicicleta(@PathVariable Integer id){
        return ResponseEntity.ok(service.buscarId(id));
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<BicicletaDtoResponse> actualizarBicicleta(Integer id,@RequestBody BicicletaUpdateDto request){
        return ResponseEntity.ok(service.actualizarBicicleta(id, request));
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarBicicleta(Integer id){
        service.eliminarBicicleta(id);
        return ResponseEntity.noContent().build();
    }

}
