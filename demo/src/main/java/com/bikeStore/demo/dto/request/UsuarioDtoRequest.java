package com.bikeStore.demo.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UsuarioDtoRequest(

        @NotBlank(message = "El nombre del usuario es obligatorio")
        @Size(min = 3, max = 50)
        @Pattern(regexp = "^[\\p{L}\\p{N}._\\-]+$", message = "El usuario solo puede contener letras, números, puntos, guiones y guiones bajos")
        String usuario,

        @NotBlank(message = "La contraseña es obligatoria")
        @Size(min = 6, message = "La contraseña debe tener minimo 6 caracteres")
        String password,

        @NotBlank(message = "El documento es obligatorio")
        @Size(min = 5, max = 20)
        @Pattern(regexp = "\\d+", message = "El documento solo puede contener números")
        String document,

        @Size(max = 10)
        @Pattern(regexp = "\\d*", message = "El teléfono solo puede contener números")
        String telefono

) {
}
