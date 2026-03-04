package com.bikeStore.demo.service;

import com.bikeStore.demo.dto.request.RolDtoRequest;
import com.bikeStore.demo.dto.response.RolDtoResponse;

import java.util.List;
import java.util.UUID;

public interface IRolService {
    // Operaciones de Creación y Actualización
    RolDtoResponse crear(RolDtoRequest dto);
    RolDtoResponse actualizar(UUID id, RolDtoRequest dto);

    // Operaciones de Consulta
    List<RolDtoResponse> listarTodos();
    RolDtoResponse buscarPorId(UUID id);
    RolDtoResponse buscarPorNombre(String nombre);

    // Operaciones de Estado/Administración
    RolDtoResponse cambiarEstado(UUID id, boolean activo);
}

