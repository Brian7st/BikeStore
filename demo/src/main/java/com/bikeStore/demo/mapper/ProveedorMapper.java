package com.bikeStore.demo.mapper;

import com.bikeStore.demo.Entity.Proveedor;
import com.bikeStore.demo.dto.request.ProveedorDtoRequest;
import com.bikeStore.demo.dto.response.ProveedorDtoResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProveedorMapper {

    @Mapping(target = "idProveedor", ignore = true)
    @Mapping(target = "activo", constant = "true")
    @Mapping(target = "entradas", ignore = true)
    Proveedor toEntity(ProveedorDtoRequest dto);

    @Mapping(target = "totalEntradas", expression = "java(proveedor.getEntradas() != null ? proveedor.getEntradas().size() : 0)")
    ProveedorDtoResponse toResponseDto(Proveedor proveedor);

    @Mapping(target = "idProveedor", ignore = true)
    @Mapping(target = "activo", ignore = true)
    @Mapping(target = "entradas", ignore = true)
    void updateFromDto(ProveedorDtoRequest dto, @MappingTarget Proveedor entity);
}
