package com.bikeStore.demo.dto.response;

import java.util.UUID;

public record RolDtoResponse(
        UUID idRol,
        String nombre,
        String descripcion,
        boolean activo
) {
}
