package com.bikeStore.demo.dto.response;

public record UsuarioDtoResponse(

        Integer idUsuario,
        String usuario,
        String document,
        String telefono
) {
}
