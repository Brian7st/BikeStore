package com.bikeStore.demo.dto.request;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class VentaDtoRequest {
    private Long idUsuario;
    private LocalDateTime fecha;
    private List<DetalleVentaDtoRequest> detalles;
}
