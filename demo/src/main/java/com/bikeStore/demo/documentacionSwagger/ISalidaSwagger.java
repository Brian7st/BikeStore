package com.bikeStore.demo.documentacionSwagger;

import com.bikeStore.demo.dto.request.SalidaBajaDtoRequest;
import com.bikeStore.demo.dto.response.SalidaDtoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.UUID;

@Tag(name = "Salidas", description = "Registro de salidas de inventario. Las salidas por VENTA se generan automáticamente; " +
        "las salidas por BAJA se registran manualmente.")
public interface ISalidaSwagger {

    @Operation(
            summary = "Registrar salida por baja",
            description = "Registra una salida manual de inventario por baja (daño, pérdida, etc.). " +
                    "Descuenta el stock de la bicicleta."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Salida por baja registrada"),
            @ApiResponse(responseCode = "400", description = "Stock insuficiente o datos inválidos"),
            @ApiResponse(responseCode = "404", description = "Bicicleta o usuario no encontrados")
    })
    ResponseEntity<SalidaDtoResponse> registrarBaja(@RequestBody SalidaBajaDtoRequest dto);

    @Operation(summary = "Listar todas las salidas (ventas + bajas)")
    @ApiResponse(responseCode = "200", description = "Lista de salidas obtenida")
    ResponseEntity<List<SalidaDtoResponse>> listarTodas();

    @Operation(summary = "Obtener salida por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Salida encontrada"),
            @ApiResponse(responseCode = "404", description = "Salida no encontrada")
    })
    ResponseEntity<SalidaDtoResponse> buscarPorId(
            @Parameter(description = "ID de la salida") @PathVariable UUID id);

    @Operation(summary = "Listar salidas asociadas a una venta")
    @ApiResponse(responseCode = "200", description = "Salidas de la venta obtenidas")
    ResponseEntity<List<SalidaDtoResponse>> listarPorVenta(
            @Parameter(description = "ID de la venta") @PathVariable UUID idVenta);
}
