package com.bikeStore.demo.dto.response;

import java.util.UUID;

public record UsuarioDtoResponse(
        UUID idUsuario,
        String userName,
        String document,
        String telefono,
        String rolNombre,
        boolean activo
) {
}
