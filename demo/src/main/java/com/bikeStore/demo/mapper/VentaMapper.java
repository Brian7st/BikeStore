package com.bikeStore.demo.mapper;

import com.bikeStore.demo.dto.request.VentaDtoRequest;
import com.bikeStore.demo.dto.response.VentaDtoResponse;
import com.bikeStore.demo.Entity.Venta;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface VentaMapper {
    @Mapping(target = "idVenta", ignore = true)
    @Mapping(target = "usuario", ignore = true)
    @Mapping(target = "detalles", ignore = true)
    Venta toEntity(VentaDtoRequest request);

    @Mapping(source = "usuario.idUsuario", target = "idUsuario")
    @Mapping(source = "usuario.userName", target = "nombreUsuario")
    VentaDtoResponse toResponseDTO(Venta entidad);
}
