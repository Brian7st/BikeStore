package com.bikeStore.demo.service;

import com.bikeStore.demo.dto.request.EntradaDtoRequest;
import com.bikeStore.demo.dto.response.EntradaDtoResponse;

import java.util.List;
import java.util.UUID;

public interface IEntradaService {
    List<EntradaDtoResponse> registrarEntrada(EntradaDtoRequest dto);
    List<EntradaDtoResponse> listarTodas();
    EntradaDtoResponse buscarPorId(UUID id);
    List<EntradaDtoResponse> listarPorProveedor(UUID idProveedor);
}
