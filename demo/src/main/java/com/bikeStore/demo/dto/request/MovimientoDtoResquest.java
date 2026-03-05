package com.bikeStore.demo.dto.request;

import com.bikeStore.demo.Enums.TipoMovimiento;
import lombok.Data;

import java.util.UUID;

@Data
public class MovimientoDtoResquest {

    private UUID idUsuario;
    private UUID idBicicleta;
    private TipoMovimiento tipo;
    private Integer cantidad;

}
