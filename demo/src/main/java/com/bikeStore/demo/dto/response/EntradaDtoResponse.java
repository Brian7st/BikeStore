package com.bikeStore.demo.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record EntradaDtoResponse(
        UUID idEntrada,
        UUID idBicicleta,
        String codigoBicicleta,
        String marcaBicicleta,
        UUID idProveedor,
        String nombreProveedor,
        UUID idUsuario,
        String nombreUsuario,
        Integer cantidad,
        LocalDateTime fecha
) {
}
