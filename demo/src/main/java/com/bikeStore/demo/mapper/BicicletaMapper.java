package com.bikeStore.demo.mapper;

import com.bikeStore.demo.Entity.Bicicleta;
import com.bikeStore.demo.dto.request.BicicletaDtoRequest;
import com.bikeStore.demo.dto.request.BicicletaUpdateDto;
import com.bikeStore.demo.dto.response.BicicletaDtoResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;


@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface BicicletaMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "activo", constant = "true")
    Bicicleta toEntity(BicicletaDtoRequest dto);

    BicicletaDtoResponse toResponseDto(Bicicleta bicicleta);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "codigo", ignore = true)
    void updateEntityFromDto(BicicletaUpdateDto dto, @MappingTarget Bicicleta entity);
}
