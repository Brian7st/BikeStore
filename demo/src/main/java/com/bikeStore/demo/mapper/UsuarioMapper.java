package com.bikeStore.demo.mapper;


import com.bikeStore.demo.Entity.Usuario;
import com.bikeStore.demo.dto.request.UsuarioDtoRequest;
import com.bikeStore.demo.dto.response.UsuarioDtoResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    @Mapping(target = "idUsuario", ignore = true)
    Usuario toEntity(UsuarioDtoRequest dto);

    UsuarioDtoResponse toResponseDto(Usuario usuario);
}
