package com.bikeStore.demo.service;


import com.bikeStore.demo.dto.request.BicicletaDtoRequest;
import com.bikeStore.demo.dto.request.BicicletaUpdateDto;
import com.bikeStore.demo.dto.response.BicicletaDtoResponse;
import java.util.List;
import java.util.UUID;

public interface IBicicletaService {
    BicicletaDtoResponse crearBicicleta(BicicletaDtoRequest dto);
    BicicletaDtoResponse buscarId(UUID id);
    List<BicicletaDtoResponse> listarTodo();
    BicicletaDtoResponse actualizarBicicleta(UUID id, BicicletaUpdateDto dto);
    void eliminarBicicleta(UUID id);

}