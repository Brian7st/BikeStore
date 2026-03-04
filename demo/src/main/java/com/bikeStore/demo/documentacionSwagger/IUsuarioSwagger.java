package com.bikeStore.demo.documentacionSwagger;

import com.bikeStore.demo.dto.request.UsuarioDtoRequest;
import com.bikeStore.demo.dto.response.UsuarioDtoResponse;
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

@Tag(name = "Usuarios", description = "Operaciones para la gestión de usuarios del sistema")
public interface IUsuarioSwagger {
    @Operation(
            summary = "Crear un nuevo usuario",
            description = "Registra un usuario en el sistema. El documento y el nombre de usuario deben ser únicos."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuario creado con éxito"),
            @ApiResponse(responseCode = "400", description = "Error en los datos de entrada o usuario duplicado")
    })
    ResponseEntity<UsuarioDtoResponse> crear(
            @RequestBody UsuarioDtoRequest dto
    );

    @Operation(summary = "Listar todos los usuarios")
    @ApiResponse(responseCode = "200", description = "Lista de usuarios obtenida")
    ResponseEntity<List<UsuarioDtoResponse>> listarTodos();

    @Operation(summary = "Obtener usuario por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario encontrado"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    ResponseEntity<UsuarioDtoResponse> buscarPorId(
            @Parameter(description = "ID único del usuario (UUID)", example = "a3f1c2d4-...")
            @PathVariable UUID id
    );

    @Operation(summary = "Actualizar un usuario existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario actualizado con éxito"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
            @ApiResponse(responseCode = "400", description = "Error en los datos de entrada")
    })
    ResponseEntity<UsuarioDtoResponse> actualizar(
            @Parameter(description = "ID único del usuario (UUID)", example = "a3f1c2d4-...")
            @PathVariable UUID id,
            @RequestBody UsuarioDtoRequest dto
    );

    @Operation(
            summary = "Eliminar un usuario",
            description = "Elimina permanentemente el usuario del sistema."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Usuario eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    ResponseEntity<Void> eliminar(
            @Parameter(description = "ID único del usuario (UUID)", example = "a3f1c2d4-...")
            @PathVariable UUID id
    );

    @Operation(
            summary = "Activar un usuario",
            description = "Cambia el estado del usuario a activo."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario activado correctamente"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    ResponseEntity<UsuarioDtoResponse> activar(
            @Parameter(description = "ID único del usuario (UUID)", example = "a3f1c2d4-...")
            @PathVariable UUID id
    );

    @Operation(
            summary = "Desactivar un usuario",
            description = "Cambia el estado del usuario a inactivo. El usuario no podrá iniciar sesión."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario desactivado correctamente"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    ResponseEntity<UsuarioDtoResponse> desactivar(
            @Parameter(description = "ID único del usuario (UUID)", example = "a3f1c2d4-...")
            @PathVariable UUID id
    );
}
