package com.bikeStore.demo.dto.response;

import com.bikeStore.demo.Enums.TipoMovimiento;

import java.time.LocalDateTime;
import java.util.UUID;

public record MovimientoDtoResponse(
        UUID idMovimiento,
        UUID idBicicleta,
        String codigoBicileta,
        String marcaBicicleta,
        UUID idUsuario,
        String nombreUsuario,
        TipoMovimiento tipo,
        Integer cantidad,
        LocalDateTime fecha,
        UUID idVenta          // null para ENTRADA, populated para SALIDA via Venta
) {
}
