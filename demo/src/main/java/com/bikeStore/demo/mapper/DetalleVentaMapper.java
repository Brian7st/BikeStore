package com.bikeStore.demo.mapper;


import com.bikeStore.demo.dto.request.DetalleVentaDtoRequest;
import com.bikeStore.demo.dto.response.DetalleVentaDtoResponse;
import com.bikeStore.demo.Entity.DetalleVenta;
import org.mapstruct.*;


@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface DetalleVentaMapper {

    @Mapping(target = "idDetalleVenta", ignore = true)
    @Mapping(target = "venta",          ignore = true)
    @Mapping(target = "bicicleta",      ignore = true)
    @Mapping(target = "totalDetalle",   ignore = true)
    DetalleVenta toEntity(DetalleVentaDtoRequest request);

    @Mapping(source = "bicicleta.id",    target = "idBicicleta")   // ✅ "id" no "idBicicleta"
    @Mapping(source = "bicicleta.marca", target = "nombreBicicleta")
    DetalleVentaDtoResponse toResponseDTO(DetalleVenta entidad);
}
