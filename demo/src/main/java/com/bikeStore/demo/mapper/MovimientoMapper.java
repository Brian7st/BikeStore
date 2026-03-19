package com.bikeStore.demo.mapper;

import com.bikeStore.demo.Entity.Movimiento;
import com.bikeStore.demo.dto.response.MovimientoDtoResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MovimientoMapper {

    @Mapping(source = "bicicleta.id", target = "idBicicleta")
    @Mapping(source = "bicicleta.codigo", target = "codigoBicileta")
    @Mapping(source = "bicicleta.marca", target = "marcaBicicleta")
    @Mapping(source = "usuario.idUsuario", target = "idUsuario")
    @Mapping(source = "usuario.userName", target = "nombreUsuario")

    MovimientoDtoResponse toResponseDto(Movimiento movimiento);

}
