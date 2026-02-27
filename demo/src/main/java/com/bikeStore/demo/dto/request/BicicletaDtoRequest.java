package com.bikeStore.demo.dto.request;

import com.bikeStore.demo.Enums.Tipo;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record BicicletaDtoRequest(
        @NotBlank(message = "El codigo es obligatorio")
        String codigo,

        @Size(min = 2, max = 30)
        String marca,
        String modelo,

        @NotNull
        Tipo tipo,

        @Positive(message = "El precio debe ser mayor a cero")
        BigDecimal valorUnitario,

        @Min(0)
        int stock
) {}
