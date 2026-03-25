package com.bikeStore.demo.mapper;


import com.bikeStore.demo.Entity.Usuario;
import com.bikeStore.demo.dto.request.UsuarioDtoRequest;
import com.bikeStore.demo.dto.response.LoginResponseDTO;
import com.bikeStore.demo.dto.response.UsuarioDtoResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface UsuarioMapper {

    @Mapping(target = "idUsuario", ignore = true)
    @Mapping(source = "usuario", target = "userName")
    @Mapping(target = "activo", constant = "true")
    Usuario toEntity(UsuarioDtoRequest dto);


    @Mapping(source = "usuario.userName", target = "username")
    @Mapping(source = "usuario.rol.nombre", target = "role")
    @Mapping(source = "usuario.idUsuario", target = "userId")
    @Mapping(source = "token", target = "token") // El token viene como parámetro externo
    LoginResponseDTO toAuthResponse(Usuario usuario, String token);

    @Mapping(source = "rol.nombre", target = "rolNombre")
    UsuarioDtoResponse toResponseDto(Usuario usuario);

}
