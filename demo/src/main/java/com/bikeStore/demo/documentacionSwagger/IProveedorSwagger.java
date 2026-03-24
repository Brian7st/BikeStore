package com.bikeStore.demo.documentacionSwagger;

import com.bikeStore.demo.dto.request.ProveedorDtoRequest;
import com.bikeStore.demo.dto.response.ProveedorDtoResponse;
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

@Tag(name = "Proveedores", description = "Gestión de proveedores de bicicletas")
public interface IProveedorSwagger {

    @Operation(summary = "Registrar un nuevo proveedor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Proveedor creado con éxito"),
            @ApiResponse(responseCode = "400", description = "Nombre duplicado o datos inválidos")
    })
    ResponseEntity<ProveedorDtoResponse> crear(@RequestBody ProveedorDtoRequest dto);

    @Operation(summary = "Listar todos los proveedores")
    @ApiResponse(responseCode = "200", description = "Lista de proveedores obtenida")
    ResponseEntity<List<ProveedorDtoResponse>> listarTodos();

    @Operation(summary = "Obtener proveedor por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Proveedor encontrado"),
            @ApiResponse(responseCode = "404", description = "Proveedor no encontrado")
    })
    ResponseEntity<ProveedorDtoResponse> buscarPorId(
            @Parameter(description = "ID del proveedor") @PathVariable UUID id);

    @Operation(summary = "Actualizar un proveedor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Proveedor actualizado"),
            @ApiResponse(responseCode = "404", description = "Proveedor no encontrado")
    })
    ResponseEntity<ProveedorDtoResponse> actualizar(
            @Parameter(description = "ID del proveedor") @PathVariable UUID id,
            @RequestBody ProveedorDtoRequest dto);

    @Operation(summary = "Activar un proveedor")
    ResponseEntity<ProveedorDtoResponse> activar(@Parameter(description = "ID del proveedor") @PathVariable UUID id);

    @Operation(summary = "Desactivar un proveedor")
    ResponseEntity<ProveedorDtoResponse> desactivar(@Parameter(description = "ID del proveedor") @PathVariable UUID id);
}
