package com.bikeStore.demo.dto.response;

import lombok.Data;
import java.math.BigDecimal;
import java.util.UUID;

@Data
public class DetalleVentaDtoResponse {
    private UUID idDetalleVenta;
    private UUID idBicicleta;
    private String nombreBicicleta;
    private String marcaBicicleta;
    private Integer cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal totalDetalle;
}
