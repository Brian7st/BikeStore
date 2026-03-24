package com.bikeStore.demo.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record EntradaDtoRequest(
        @NotNull(message = "El ID del proveedor es obligatorio")
        UUID idProveedor,

        @NotNull(message = "El ID del usuario es obligatorio")
        UUID idUsuario,

        @NotEmpty(message = "Debe incluir al menos un item")
        @Valid
        List<EntradaItemDto> items
) {
}
