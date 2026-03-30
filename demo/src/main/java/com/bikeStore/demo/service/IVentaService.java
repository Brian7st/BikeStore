package com.bikeStore.demo.service;

import com.bikeStore.demo.dto.request.VentaDtoRequest;
import com.bikeStore.demo.dto.response.VentaDtoResponse;

import java.util.List;
import java.util.UUID;

public interface IVentaService {
    VentaDtoResponse registrarVenta(VentaDtoRequest request);
    List<VentaDtoResponse> listarTodas();
    VentaDtoResponse buscarPorId(UUID id);
    List<VentaDtoResponse> listarPorUsuario(UUID idUsuario);
}
