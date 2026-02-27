package com.bikeStore.demo.mapper;


import com.bikeStore.demo.Entity.Bicicleta;
import com.bikeStore.demo.dto.request.BicicletaDtoRequest;
import com.bikeStore.demo.dto.response.BicicletaDtoResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface BicicletaMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "activo", constant = "true")
    Bicicleta toEntity(BicicletaDtoRequest dto);

    BicicletaDtoResponse toResponseDto(Bicicleta bicicleta);
}
