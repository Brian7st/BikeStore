package com.bikeStore.demo.dto.response;

import com.bikeStore.demo.Entity.Bicicleta;
import com.bikeStore.demo.Entity.Usuario;
import com.bikeStore.demo.Enums.TipoMovimiento;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

public record MovimientoDtoResponse(
        UUID idMovimiento,


        UUID idBicicleta,
        String codigoBicileta,
        String marcaBicicleta,


        Integer idUsuario,
        String nombreUsuario,

        TipoMovimiento tipo,
        Integer cantidad,
        LocalDateTime fecha
) {
}
