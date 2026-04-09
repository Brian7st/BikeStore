package com.bikeStore.demo.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record VentaDtoRequest(
        @NotNull(message = "El ID del usuario es obligatorio")
        UUID idUsuario,

        LocalDateTime fecha,

        @NotNull(message = "La lista de detalles es obligatoria")
        @NotEmpty(message = "La venta debe tener al menos un producto")
        @Valid
        List<DetalleVentaDtoRequest> detalles,

        // Opcional: si se provee, se envía la factura PDF por correo al cliente
        String emailCliente) {
}
