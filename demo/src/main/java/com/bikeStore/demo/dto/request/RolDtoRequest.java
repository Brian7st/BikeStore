package com.bikeStore.demo.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RolDtoRequest(
        @NotBlank(message = "El nombre del rol es obligatorio")
        @Pattern(
                regexp = "ADMIN|EMPLEADO",
                message = "El rol debe ser ADMIN O EMPLEADO"
        )
        String nombre,

        @Size(max = 100, message = "La descripcion no puede superar los 100 caracteres")
        @Pattern(regexp = "^[\\p{L}\\p{N}\\s.,'\\-]*$", message = "La descripción contiene caracteres no permitidos")
        String descripcion
) {
}
