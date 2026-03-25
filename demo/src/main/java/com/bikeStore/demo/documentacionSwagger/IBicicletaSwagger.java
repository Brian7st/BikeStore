package com.bikeStore.demo.documentacionSwagger;



import com.bikeStore.demo.dto.request.BicicletaDtoRequest;
import com.bikeStore.demo.dto.request.BicicletaUpdateDto;
import com.bikeStore.demo.dto.response.BicicletaDtoResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@Tag(name = "Bicicletas", description = "Operaciones para la gestión del inventario de bicicletas")
public interface IBicicletaSwagger {

    @Operation(
            summary = "Registrar una nueva bicicleta",
            description = "Guarda una bicicleta en el sistema. El código debe ser único y el estado inicial será 'activo'."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Bicicleta creada con éxito"),
            @ApiResponse(responseCode = "400", description = "Error en los datos de entrada o código duplicado")
    })
    ResponseEntity<BicicletaDtoResponse> crearBicicleta(
            @RequestBody BicicletaDtoRequest request
    );


    @Operation(summary = "Actualizar una bicicleta existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Actualización exitosa"),
            @ApiResponse(responseCode = "404", description = "Bicicleta no encontrada")
    })
    ResponseEntity<BicicletaDtoResponse> actualizarBicicleta(
            @Parameter(description = "ID de la bicicleta", example = "1") @PathVariable UUID id,
            @RequestBody BicicletaUpdateDto request
    );

    @Operation(summary = "Eliminación lógica de una bicicleta")
    @ApiResponse(responseCode = "204", description = "Bicicleta desactivada correctamente")
    ResponseEntity<Void> eliminarBicicleta(
            @Parameter(description = "ID de la bicicleta", example = "1") @PathVariable UUID id
    );

    @Operation(summary = "Listar todas las bicicletas activas")
    @ApiResponse(responseCode = "200", description = "Lista de bicicletas obtenida")
    ResponseEntity<List<BicicletaDtoResponse>> listarTodoBicicleta();

    @Operation(summary = "Obtener bicicleta por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Bicicleta encontrada"),
            @ApiResponse(responseCode = "404", description = "Bicicleta no encontrada")
    })
    ResponseEntity<BicicletaDtoResponse> obtenerPorIdBicicleta(
            @Parameter(description = "ID único de la bicicleta", example = "1") @PathVariable UUID id
    );
}