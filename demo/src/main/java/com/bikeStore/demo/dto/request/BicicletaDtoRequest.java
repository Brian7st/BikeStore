package com.bikeStore.demo.dto.request;

import com.bikeStore.demo.Enums.Tipo;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record BicicletaDtoRequest(
        @NotBlank(message = "El codigo es obligatorio")
        @Pattern(regexp = "^[\\p{L}\\p{N}\\-]+$", message = "El codigo solo puede contener letras, números y guiones")
        String codigo,

        @Size(min = 2, max = 30)
        @Pattern(regexp = "^[\\p{L}\\p{N}\\s.\\-]+$", message = "La marca contiene caracteres no permitidos")
        String marca,

        @Pattern(regexp = "^[\\p{L}\\p{N}\\s.\\-]+$", message = "El modelo contiene caracteres no permitidos")
        String modelo,

        @NotNull
        Tipo tipo,

        @Positive(message = "El precio debe ser mayor a cero")
        BigDecimal valorUnitario

) {}
