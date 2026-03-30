package com.bikeStore.demo.mapper;

import com.bikeStore.demo.Entity.Salida;
import com.bikeStore.demo.dto.response.SalidaDtoResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SalidaMapper {

    @Mapping(source = "bicicleta.id", target = "idBicicleta")
    @Mapping(source = "bicicleta.codigo", target = "codigoBicicleta")
    @Mapping(source = "bicicleta.marca", target = "marcaBicicleta")
    @Mapping(source = "usuario.id", target = "idUsuario")
    @Mapping(source = "usuario.userName", target = "nombreUsuario")
    SalidaDtoResponse toResponseDto(Salida salida);
}
