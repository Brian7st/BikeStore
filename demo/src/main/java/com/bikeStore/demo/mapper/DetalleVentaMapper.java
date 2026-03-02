package com.bikeStore.demo.mapper;

import com.bikeStore.demo.dto.request.DetalleVentaDtoRequest;
import com.bikeStore.demo.dto.response.DetalleVentaDtoResponse;
import com.bikeStore.demo.Entity.DetalleVenta;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
public interface DetalleVentaMapper {
    public DetalleVenta toEntity(DetalleVentaDtoRequest){
        if (request == null) return null;
        DetalleVenta detalle = new DetalleVenta();
    }
}
