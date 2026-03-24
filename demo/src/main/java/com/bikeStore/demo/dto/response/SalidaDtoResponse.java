package com.bikeStore.demo.dto.response;

import com.bikeStore.demo.Enums.TipoSalida;

import java.time.LocalDateTime;
import java.util.UUID;

public record SalidaDtoResponse(
        UUID idSalida,
        UUID idBicicleta,
        String codigoBicicleta,
        String marcaBicicleta,
        UUID idUsuario,
        String nombreUsuario,
        TipoSalida tipoSalida,
        Integer cantidad,
        String observacion,
        LocalDateTime fecha
) {
}
