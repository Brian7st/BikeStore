package com.bikeStore.demo.dto.request;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "El nombre de usuario es obligatorio")
        String userName,

        @NotBlank(message = "La contraseña es obligatoria")
        String password
) {
}
