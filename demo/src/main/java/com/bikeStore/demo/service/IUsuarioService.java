package com.bikeStore.demo.service;

import com.bikeStore.demo.dto.request.UsuarioDtoRequest;
import com.bikeStore.demo.dto.response.UsuarioDtoResponse;
import java.util.List;

public interface IUsuarioService {
    UsuarioDtoResponse crear(UsuarioDtoRequest dto);
    List<UsuarioDtoResponse> listarTodos();
    UsuarioDtoResponse buscarPorId(Integer id);
    UsuarioDtoResponse actualizar(Integer id, UsuarioDtoRequest dto);
    void eliminar(Integer id);
}