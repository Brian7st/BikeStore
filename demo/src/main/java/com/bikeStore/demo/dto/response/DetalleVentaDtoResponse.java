package com.bikeStore.demo.dto.response;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class DetalleVentaDtoResponse {
    private Long idDetalleVenta;
    private Long idBicicleta;
    private String nombreBicicleta;
    private Integer cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal totalDetalle;
}
