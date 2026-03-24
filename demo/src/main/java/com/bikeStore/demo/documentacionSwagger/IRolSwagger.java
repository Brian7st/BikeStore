package com.bikeStore.demo.documentacionSwagger;

import com.bikeStore.demo.dto.request.RolDtoRequest;
import com.bikeStore.demo.dto.response.RolDtoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;  // ← el correcto, NO el de hibernate

@Tag(name = "Roles", description = "Gestión de roles del sistema. Solo existen dos roles: ADMIN y EMPLEADO")
public interface IRolSwagger {

    @Operation(
            summary = "Crear un nuevo rol",
            description = "Registra un rol en el sistema. Solo se permiten los valores ADMIN o EMPLEADO y no pueden repetirse."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Rol creado con éxito"),
            @ApiResponse(responseCode = "400", description = "El rol ya existe o el nombre no es válido")
    })
    ResponseEntity<RolDtoResponse> crear(
            @RequestBody RolDtoRequest dto
    );

    @Operation(summary = "Listar todos los roles")
    @ApiResponse(responseCode = "200", description = "Lista de roles obtenida")
    ResponseEntity<List<RolDtoResponse>> listarTodos();

    @Operation(summary = "Obtener rol por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rol encontrado"),
            @ApiResponse(responseCode = "404", description = "Rol no encontrado")
    })
    ResponseEntity<RolDtoResponse> buscarPorId(
            @Parameter(description = "ID único del rol (UUID)", example = "b7e2a1f3-...")
            @PathVariable UUID id   // ← java.util.UUID
    );

    @Operation(
            summary = "Obtener rol por nombre",
            description = "Busca un rol usando su nombre. Valores posibles: ADMIN, EMPLEADO"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rol encontrado"),
            @ApiResponse(responseCode = "404", description = "Rol no encontrado")
    })
    ResponseEntity<RolDtoResponse> buscarPorNombre(
            @Parameter(description = "Nombre del rol", example = "ADMIN")
            @PathVariable String nombre
    );

    @Operation(
            summary = "Actualizar un rol",
            description = "Permite actualizar la descripción del rol."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rol actualizado con éxito"),
            @ApiResponse(responseCode = "404", description = "Rol no encontrado"),
            @ApiResponse(responseCode = "400", description = "Error en los datos de entrada")
    })
    ResponseEntity<RolDtoResponse> actualizar(
            @Parameter(description = "ID único del rol (UUID)", example = "b7e2a1f3-...")
            @PathVariable UUID id,  // ← java.util.UUID
            @RequestBody RolDtoRequest dto
    );

    @Operation(
            summary = "Activar un rol",
            description = "Cambia el estado del rol a activo."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rol activado correctamente"),
            @ApiResponse(responseCode = "404", description = "Rol no encontrado")
    })
    @PatchMapping("/{id}/activar")
    ResponseEntity<RolDtoResponse> activar(
            @Parameter(description = "ID único del rol (UUID)", example = "b7e2a1f3-...")
            @PathVariable UUID id   // ← java.util.UUID
    );

    @Operation(
            summary = "Desactivar un rol",
            description = "No se puede desactivar si tiene usuarios activos asignados."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rol desactivado correctamente"),
            @ApiResponse(responseCode = "400", description = "El rol tiene usuarios activos asignados"),
            @ApiResponse(responseCode = "404", description = "Rol no encontrado")
    })
    @PatchMapping("/{id}/desactivar")
    ResponseEntity<RolDtoResponse> desactivar(
            @Parameter(description = "ID único del rol (UUID)", example = "b7e2a1f3-...")
            @PathVariable UUID id   // ← java.util.UUID
    );
}