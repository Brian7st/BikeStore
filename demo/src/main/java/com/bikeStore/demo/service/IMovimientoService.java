package com.bikeStore.demo.service;



import com.bikeStore.demo.dto.request.MovimientoDtoResquest;
import com.bikeStore.demo.dto.response.MovimientoDtoResponse;


import java.util.List;

public interface IMovimientoService {
    MovimientoDtoResponse registrar(MovimientoDtoResquest dto);
    List<MovimientoDtoResponse> listarTodos();
    MovimientoDtoResponse buscarPorId(Integer id);
}