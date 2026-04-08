package com.bikeStore.demo.dto.request;

import com.bikeStore.demo.Enums.Tipo;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record BicicletaUpdateDto(
        @Size(min = 2, max = 30)
        @Pattern(regexp = "^[\\p{L}\\p{N}\\s.\\-]+$", message = "La marca contiene caracteres no permitidos")
        String marca,

        @Pattern(regexp = "^[\\p{L}\\p{N}\\s.\\-]+$", message = "El modelo contiene caracteres no permitidos")
        String modelo,

        Tipo tipo,

        @Positive(message = "El precio debe ser mayor a cero")
        BigDecimal valorUnitario,

        @Min(0)
        int stock
) {}
