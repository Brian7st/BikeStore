package com.bikeStore.demo.documentacionSwagger;

import com.bikeStore.demo.dto.request.EntradaDtoRequest;
import com.bikeStore.demo.dto.response.EntradaDtoResponse;
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

@Tag(name = "Entradas", description = "Registro de entradas de inventario desde proveedores. Soporta registro en lote.")
public interface IEntradaSwagger {

    @Operation(
            summary = "Registrar entrada de inventario (lote)",
            description = "Registra múltiples entradas de bicicletas desde un proveedor en una sola operación. " +
                    "Incrementa el stock de cada bicicleta automáticamente."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Entradas registradas con éxito"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos, proveedor inactivo, o bicicleta inactiva"),
            @ApiResponse(responseCode = "404", description = "Proveedor, usuario o bicicleta no encontrados")
    })
    ResponseEntity<List<EntradaDtoResponse>> registrar(@RequestBody EntradaDtoRequest dto);

    @Operation(summary = "Listar todas las entradas")
    @ApiResponse(responseCode = "200", description = "Lista de entradas obtenida")
    ResponseEntity<List<EntradaDtoResponse>> listarTodas();

    @Operation(summary = "Obtener entrada por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Entrada encontrada"),
            @ApiResponse(responseCode = "404", description = "Entrada no encontrada")
    })
    ResponseEntity<EntradaDtoResponse> buscarPorId(
            @Parameter(description = "ID de la entrada") @PathVariable UUID id);

    @Operation(summary = "Listar entradas por proveedor")
    @ApiResponse(responseCode = "200", description = "Entradas del proveedor obtenidas")
    ResponseEntity<List<EntradaDtoResponse>> listarPorProveedor(
            @Parameter(description = "ID del proveedor") @PathVariable UUID idProveedor);
}
