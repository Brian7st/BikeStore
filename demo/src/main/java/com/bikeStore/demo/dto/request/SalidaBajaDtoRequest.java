package com.bikeStore.demo.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record SalidaBajaDtoRequest(
        @NotNull(message = "El ID de la bicicleta es obligatorio")
        UUID idBicicleta,

        @NotNull(message = "El ID del usuario es obligatorio")
        UUID idUsuario,

        @NotNull(message = "La cantidad es obligatoria")
        @Min(value = 1, message = "La cantidad debe ser al menos 1")
        Integer cantidad,

        @Size(max = 255, message = "La observación no puede superar 255 caracteres")
        @Pattern(regexp = "^[\\p{L}\\p{N}\\s.,'\\-]*$", message = "La observación contiene caracteres no permitidos")
        String observacion
) {
}
