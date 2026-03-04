package com.bikeStore.demo.mapper;

import com.bikeStore.demo.Entity.Rol;
import com.bikeStore.demo.dto.request.RolDtoRequest;
import com.bikeStore.demo.dto.response.RolDtoResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface RolMapper {

    @Mapping(target = "idRol", ignore = true)
    @Mapping(target = "activo",constant = "true")
    @Mapping(target = "usuario", ignore = true)
    Rol toEntity(RolDtoRequest dto);

    @Mapping(target = "tottalUsuarios", expression = "java(rol.getUsuarios() != null ? rol.getUsuarios().size() : 0)")
    RolDtoResponse toResponseDto(Rol rol);

    @Mapping(target = "idRol", ignore = true)
    @Mapping(target = "activo", ignore = true)
    @Mapping(target = "usuarios",ignore = true)
    void updateFromDto(RolDtoRequest dto, @MappingTarget Rol entity);
}
