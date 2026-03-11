package com.bikeStore.demo.dto.response;

import java.util.UUID;

public record UsuarioDtoResponse(

        UUID idUsuario,
        String usuario,
        String document,
        String telefono
) {
}
