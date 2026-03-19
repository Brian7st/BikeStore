package com.bikeStore.demo.service;

import com.bikeStore.demo.Entity.Bicicleta;
import com.bikeStore.demo.Entity.Usuario;
import com.bikeStore.demo.Entity.Venta;
import com.bikeStore.demo.dto.request.MovimientoDtoResquest;
import com.bikeStore.demo.dto.response.MovimientoDtoResponse;

import java.util.List;
import java.util.UUID;

public interface IMovimientoService {
    MovimientoDtoResponse registrar(MovimientoDtoResquest dto);
    List<MovimientoDtoResponse> listarTodos();
    MovimientoDtoResponse buscarPorId(UUID id);

    /**
     * Método interno exclusivo para ser invocado desde VentaServiceImpl.
     * Registra automáticamente una SALIDA ligada a la Venta que la originó.
     */
    void registrarSalida(Bicicleta bicicleta, Usuario usuario, int cantidad, Venta venta);
}