package com.bikeStore.demo.dto.request;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record VentaDtoRequest(
        UUID idUsuario,
        LocalDateTime fecha,
        List<DetalleVentaDtoRequest> detalles) {
}
