package com.bikeStore.demo.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record EntradaItemDto(
        @NotNull(message = "El ID de la bicicleta es obligatorio")
        UUID idBicicleta,

        @NotNull(message = "La cantidad es obligatoria")
        @Min(value = 1, message = "La cantidad debe ser al menos 1")
        Integer cantidad
) {
}
