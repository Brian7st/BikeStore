package com.bikeStore.demo.service;

import com.bikeStore.demo.dto.request.ProveedorDtoRequest;
import com.bikeStore.demo.dto.response.ProveedorDtoResponse;

import java.util.List;
import java.util.UUID;

public interface IProveedorService {
    ProveedorDtoResponse crear(ProveedorDtoRequest dto);
    List<ProveedorDtoResponse> listarTodos();
    ProveedorDtoResponse buscarPorId(UUID id);
    ProveedorDtoResponse actualizar(UUID id, ProveedorDtoRequest dto);
    ProveedorDtoResponse cambiarEstado(UUID id, boolean activo);
}
