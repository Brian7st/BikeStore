package com.bikeStore.demo.documentacionSwagger;

import com.bikeStore.demo.dto.request.MovimientoDtoResquest;
import com.bikeStore.demo.dto.response.MovimientoDtoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.UUID;

@Tag(name = "Movimientos", description = "Gestión de movimientos de inventario. Registra entradas y salidas de bicicletas y actualiza el stock automáticamente")
public interface IMovimientoSwagger {
    @Operation(
            summary = "Registrar un movimiento",
            description = "Registra una entrada o salida de bicicletas. " +
                    "Si es ENTRADA suma al stock, si es SALIDA lo descuenta. " +
                    "No se permite SALIDA si el stock es insuficiente."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Movimiento registrado con éxito"),
            @ApiResponse(responseCode = "400", description = "Stock insuficiente o datos inválidos"),
            @ApiResponse(responseCode = "404", description = "Bicicleta o usuario no encontrado")
    })
    ResponseEntity<MovimientoDtoResponse> registrar(
            @RequestBody MovimientoDtoResquest dto
    );

    @Operation(
            summary = "Listar todos los movimientos",
            description = "Retorna el historial completo de entradas y salidas de inventario."
    )
    @ApiResponse(responseCode = "200", description = "Lista de movimientos obtenida")
    ResponseEntity<List<MovimientoDtoResponse>> listarTodos();

    @Operation(
            summary = "Obtener movimiento por ID",
            description = "Retorna el detalle de un movimiento específico incluyendo bicicleta, usuario, tipo y fecha."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Movimiento encontrado"),
            @ApiResponse(responseCode = "404", description = "Movimiento no encontrado")
    })
    ResponseEntity<MovimientoDtoResponse> buscarPorId(
            @Parameter(description = "ID único del movimiento", example = "1")
            @PathVariable UUID id
    );
}
