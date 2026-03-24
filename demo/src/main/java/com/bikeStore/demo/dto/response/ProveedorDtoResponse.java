package com.bikeStore.demo.dto.response;

import java.util.UUID;

public record ProveedorDtoResponse(
        UUID idProveedor,
        String nombre,
        String contacto,
        String telefono,
        String email,
        boolean activo,
        int totalEntradas
) {
}
