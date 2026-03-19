package com.bikeStore.demo.service.impl;

import com.bikeStore.demo.Entity.DetalleVenta;
import com.bikeStore.demo.dto.response.DetalleVentaDtoResponse;
import com.bikeStore.demo.mapper.DetalleVentaMapper;
import com.bikeStore.demo.repository.DetalleVentaRepository;
import com.bikeStore.demo.service.IDetalleVentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DetalleVentaServiceImpl implements IDetalleVentaService{
    @Autowired
    private DetalleVentaRepository detalleRepo;

    @Autowired
    private DetalleVentaMapper detalleMapper;

    @Override
    public List<DetalleVentaDtoResponse> listarTodos() {
        return detalleRepo.findAll().stream()
                .map(detalleMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public DetalleVentaDtoResponse buscarPorId(UUID id) {
        DetalleVenta detalle = detalleRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Detalle de venta no encontrado"));
        return detalleMapper.toResponseDTO(detalle);
    }

    @Override
    public List<DetalleVentaDtoResponse> listarPorVenta(UUID idVenta) {
        return detalleRepo.findByVenta_IdVenta(idVenta).stream()
                .map(detalleMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
}
