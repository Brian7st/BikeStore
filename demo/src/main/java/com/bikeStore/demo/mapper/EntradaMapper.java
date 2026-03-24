package com.bikeStore.demo.mapper;

import com.bikeStore.demo.Entity.Entrada;
import com.bikeStore.demo.dto.response.EntradaDtoResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EntradaMapper {

    @Mapping(source = "bicicleta.id", target = "idBicicleta")
    @Mapping(source = "bicicleta.codigo", target = "codigoBicicleta")
    @Mapping(source = "bicicleta.marca", target = "marcaBicicleta")
    @Mapping(source = "proveedor.id", target = "idProveedor")
    @Mapping(source = "proveedor.nombre", target = "nombreProveedor")
    @Mapping(source = "usuario.id", target = "idUsuario")
    @Mapping(source = "usuario.userName", target = "nombreUsuario")
    EntradaDtoResponse toResponseDto(Entrada entrada);
}
