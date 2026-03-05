package com.bikeStore.demo.dto.request;

import java.math.BigDecimal;
import java.util.UUID;
public record DetalleVentaDtoRequest(
        UUID idBicicleta,
        Integer cantidad,
        BigDecimal precioUnitario) {
}
