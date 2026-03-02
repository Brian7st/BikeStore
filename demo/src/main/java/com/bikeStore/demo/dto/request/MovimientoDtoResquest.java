package com.bikeStore.demo.dto.request;

import com.bikeStore.demo.Enums.TipoMovimiento;
import lombok.Data;

@Data
public class MovimientoDtoResquest {

    private Integer idUsuario;
    private Integer idBicicleta;
    private TipoMovimiento tipo;
    private Integer cantidad;

}
