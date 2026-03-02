package com.bikeStore.demo.dto.request;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class DetalleVentaDtoRequest {
    private Long idBicicleta;
    private Integer cantidad;
    private BigDecimal precioUnitario;
}
