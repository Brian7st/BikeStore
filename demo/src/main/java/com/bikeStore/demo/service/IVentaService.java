package com.bikeStore.demo.service;

import com.bikeStore.demo.dto.request.VentaDtoRequest;
import com.bikeStore.demo.dto.response.VentaDtoResponse;

public interface IVentaService {
    VentaDtoResponse registrarVenta(VentaDtoRequest request);
}
