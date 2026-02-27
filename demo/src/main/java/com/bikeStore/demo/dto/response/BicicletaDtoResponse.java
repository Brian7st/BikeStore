package com.bikeStore.demo.dto.response;

import com.bikeStore.demo.Enums.Tipo;

import java.math.BigDecimal;

public record BicicletaDtoResponse(
   Integer id,
   String codigo,
   String marca,
   String modelo,
   Tipo tipo,
   BigDecimal valorUnitario,
   int stock,
   boolean activo
) {}
