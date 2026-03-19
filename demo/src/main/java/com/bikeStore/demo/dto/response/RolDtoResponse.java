package com.bikeStore.demo.dto.response;

import java.util.UUID;

public record RolDtoResponse(
        UUID idRol,
        String nombre,
        String descripcion,
        boolean activo,
        int totalUsuarios   // ✅ Faltaba este campo — RolMapper lo calcula con expresión
) {
}
