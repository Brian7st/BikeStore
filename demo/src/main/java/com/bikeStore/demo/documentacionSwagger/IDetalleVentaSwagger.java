package com.bikeStore.demo.documentacionSwagger;

import com.bikeStore.demo.dto.response.DetalleVentaDtoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.UUID;

@Tag(name = "Detalles de Venta", description = "Consultas específicas sobre los ítems vendidos")
public interface IDetalleVentaSwagger {
    @Operation(summary = "Listar todos los detalles de ventas registrados")
    @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente")
    ResponseEntity<List<DetalleVentaDtoResponse>> listarTodos();

    @Operation(summary = "Obtener un detalle específico por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Detalle encontrado"),
            @ApiResponse(responseCode = "404", description = "ID de detalle no existe")
    })
    ResponseEntity<DetalleVentaDtoResponse> buscarPorId(
            @Parameter(description = "ID numérico del detalle", example = "1") @PathVariable UUID id
    );

    @Operation(summary = "Listar todos los productos de una factura (Venta) específica")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Detalles de la venta obtenidos"),
            @ApiResponse(responseCode = "404", description = "El UUID de la venta no existe")
    })
    ResponseEntity<List<DetalleVentaDtoResponse>> listarPorVenta(
            @Parameter(description = "UUID de la venta", example = "550e8400-e29b-41d4-a716-446655440000")
            @PathVariable UUID idVenta
    );
}
