package com.bikeStore.demo.mapper;


import com.bikeStore.demo.Entity.Usuario;
import com.bikeStore.demo.dto.request.UsuarioDtoRequest;
import com.bikeStore.demo.dto.response.LoginResponseDTO;
import com.bikeStore.demo.dto.response.UsuarioDtoResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    @Mapping(target = "idUsuario", ignore = true)
    Usuario toEntity(UsuarioDtoRequest dto);


    @Mapping(source = "usuario", target = "username")
    @Mapping(source = "rol.nombreRol", target = "role")
    @Mapping(source = "idUsuario", target = "userId")
    @Mapping(source = "token", target = "token") // El token viene como parámetro externo
    LoginResponseDTO toAuthResponse(Usuario usuario, String token);

}
