package com.bikeStore.demo.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ProveedorDtoRequest(
        @NotBlank(message = "El nombre del proveedor es obligatorio")
        @Size(max = 100)
        String nombre,

        @Size(max = 100)
        String contacto,

        @Size(max = 20)
        String telefono,

        @Size(max = 100)
        String email
) {
}
