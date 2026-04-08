package com.bikeStore.demo.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ProveedorDtoRequest(
        @NotBlank(message = "El nombre del proveedor es obligatorio")
        @Size(max = 100)
        @Pattern(regexp = "^[\\p{L}\\p{N}\\s.,'\\-]+$", message = "El nombre contiene caracteres no permitidos")
        String nombre,

        @Size(max = 100)
        @Pattern(regexp = "^[\\p{L}\\p{N}\\s.,'\\-]*$", message = "El contacto contiene caracteres no permitidos")
        String contacto,

        @Size(max = 20)
        @Pattern(regexp = "\\d*", message = "El teléfono solo puede contener números")
        String telefono,

        @Size(max = 100)
        @Email(message = "El email no tiene un formato válido")
        String email
) {}
