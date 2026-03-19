package com.bikeStore.demo.dto.response;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class VentaDtoResponse {
    private UUID idVenta;
    private UUID idUsuario;
    private String nombreUsuario;
    private LocalDateTime fecha;
    private BigDecimal totalVenta;
    private List<DetalleVentaDtoResponse> detalles;
}
