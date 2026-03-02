package com.bikeStore.demo.service;


import com.bikeStore.demo.dto.request.BicicletaDtoRequest;
import com.bikeStore.demo.dto.request.BicicletaUpdateDto;
import com.bikeStore.demo.dto.response.BicicletaDtoResponse;
import java.util.List;

public interface IBicicletaService {
    BicicletaDtoResponse crearBicicleta(BicicletaDtoRequest dto);
    BicicletaDtoResponse buscarId(Integer id);
    List<BicicletaDtoResponse> listarTodo();
    BicicletaDtoResponse actualizarBicicleta(Integer id, BicicletaUpdateDto dto);
    void eliminarBicicleta(Integer id);
}