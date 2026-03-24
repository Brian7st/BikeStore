package com.bikeStore.demo.service;

import com.bikeStore.demo.dto.request.SalidaBajaDtoRequest;
import com.bikeStore.demo.dto.response.SalidaDtoResponse;

import java.util.List;
import java.util.UUID;

public interface ISalidaService {
    SalidaDtoResponse registrarBaja(SalidaBajaDtoRequest dto);
    List<SalidaDtoResponse> listarTodas();
    SalidaDtoResponse buscarPorId(UUID id);
    List<SalidaDtoResponse> listarPorVenta(UUID idVenta);
}
