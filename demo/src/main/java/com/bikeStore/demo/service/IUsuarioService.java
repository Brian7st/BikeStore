package com.bikeStore.demo.service;

import com.bikeStore.demo.dto.request.UsuarioDtoRequest;
import com.bikeStore.demo.dto.response.UsuarioDtoResponse;
import java.util.List;
import java.util.UUID;

public interface IUsuarioService {
    UsuarioDtoResponse registrarUsuario(UsuarioDtoRequest dto);
    List<UsuarioDtoResponse> listarTodos();
    UsuarioDtoResponse buscarPorId(UUID id);
    UsuarioDtoResponse actualizar(UUID id, UsuarioDtoRequest dto);
    void eliminar(UUID id);
    UsuarioDtoResponse cambiarEstado(UUID id, boolean activo);
}