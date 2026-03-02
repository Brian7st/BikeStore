package com.bikeStore.demo.dto.response;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class VentaDtoResponse {
    private Long idVenta;
    private Long idUsuario;
    private String nombreUsuario;
    private LocalDateTime fecha;
    private BigDecimal totalVenta;
    private List<DetalleVentaDtoResponse> detalles;
}
