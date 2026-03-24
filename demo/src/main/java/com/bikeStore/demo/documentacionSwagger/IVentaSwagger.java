package com.bikeStore.demo.documentacionSwagger;

import com.bikeStore.demo.dto.request.VentaDtoRequest;
import com.bikeStore.demo.dto.response.VentaDtoResponse;
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

@Tag(name = "Ventas", description = "Operaciones para el registro y gestión de ventas de bicicletas")
public interface IVentaSwagger {

    @Operation(
            summary = "Registrar una nueva venta",
            description = "Procesa una venta completa: valida al usuario, verifica stock, " +
                    "descuenta inventario, crea registros de salida y calcula los totales automáticamente."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Venta registrada con éxito"),
            @ApiResponse(responseCode = "400", description = "Error en los datos de entrada o falta de stock"),
            @ApiResponse(responseCode = "404", description = "Usuario o Bicicleta no encontrados")
    })
    ResponseEntity<VentaDtoResponse> registrarVenta(@RequestBody VentaDtoRequest request);

    @Operation(summary = "Listar todas las ventas")
    @ApiResponse(responseCode = "200", description = "Lista de ventas obtenida")
    ResponseEntity<List<VentaDtoResponse>> listarTodas();

    @Operation(summary = "Obtener venta por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Venta encontrada"),
            @ApiResponse(responseCode = "404", description = "Venta no encontrada")
    })
    ResponseEntity<VentaDtoResponse> buscarPorId(
            @Parameter(description = "ID único de la venta") @PathVariable UUID id
    );

    @Operation(summary = "Listar ventas por usuario")
    @ApiResponse(responseCode = "200", description = "Ventas del usuario obtenidas")
    ResponseEntity<List<VentaDtoResponse>> listarPorUsuario(
            @Parameter(description = "ID del usuario") @PathVariable UUID idUsuario
    );
}
