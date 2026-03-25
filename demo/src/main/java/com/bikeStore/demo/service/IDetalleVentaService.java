package com.bikeStore.demo.service;

import com.bikeStore.demo.dto.response.DetalleVentaDtoResponse;
import java.util.List;
import java.util.UUID;

public interface IDetalleVentaService {
    List<DetalleVentaDtoResponse> listarTodos();
    DetalleVentaDtoResponse buscarPorId(UUID id);
    List<DetalleVentaDtoResponse> listarPorVenta(UUID idVenta);
}
