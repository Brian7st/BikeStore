package com.bikeStore.demo.dto.response;

import com.bikeStore.demo.Entity.Bicicleta;
import com.bikeStore.demo.Entity.Usuario;
import com.bikeStore.demo.Enums.TipoMovimiento;
import jakarta.persistence.*;

import java.time.LocalDateTime;

public record MovimientoDtoResponse(
        Integer idMovimiento,

        String idbicicleta,

        Integer idusuario,

        TipoMovimiento tipo,

        Integer cantidad,

        LocalDateTime fecha
) {
}
