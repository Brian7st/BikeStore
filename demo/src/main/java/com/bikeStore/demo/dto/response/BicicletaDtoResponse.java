package com.bikeStore.demo.dto.response;

import com.bikeStore.demo.Enums.Tipo;

import java.math.BigDecimal;
import java.util.UUID;

public record BicicletaDtoResponse(
   UUID id,
   String codigo,
   String marca,
   String modelo,
   Tipo tipo,
   BigDecimal valorUnitario,
   int stock,
   boolean activo
) {}
