package com.bikeStore.demo.documentacionSwagger;

import com.bikeStore.demo.dto.request.VentaDtoRequest;
import com.bikeStore.demo.dto.response.VentaDtoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Ventas", description = "Operaciones para el registro y gestión de ventas de bicicletas")
public interface IVentaSwagger {
    @Operation(
            summary = "Registrar una nueva venta",
            description = "Procesa una venta completa, valida al usuario y calcula los totales automáticamente."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Venta registrada con éxito"),
            @ApiResponse(responseCode = "400", description = "Error en los datos de entrada o falta de stock"),
            @ApiResponse(responseCode = "404", description = "Usuario o Bicicleta no encontrados")
    })
    @PostMapping
    ResponseEntity<VentaDtoResponse> registrarVenta(@RequestBody VentaDtoRequest request);
}
