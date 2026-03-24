package com.bikeStore.demo.service.impl;

import com.bikeStore.demo.Entity.DetalleVenta;
import com.bikeStore.demo.dto.response.DetalleVentaDtoResponse;
import com.bikeStore.demo.mapper.DetalleVentaMapper;
import com.bikeStore.demo.repository.DetalleVentaRepository;
import com.bikeStore.demo.service.IDetalleVentaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DetalleVentaServiceImpl implements IDetalleVentaService {

    private final DetalleVentaRepository detalleRepo;
    private final DetalleVentaMapper detalleMapper;

    @Override
    @Transactional(readOnly = true)
    public List<DetalleVentaDtoResponse> listarTodos() {
        return detalleRepo.findAll().stream()
                .map(detalleMapper::toResponseDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public DetalleVentaDtoResponse buscarPorId(UUID id) {
        DetalleVenta detalle = detalleRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Detalle de venta no encontrado"));
        return detalleMapper.toResponseDTO(detalle);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DetalleVentaDtoResponse> listarPorVenta(UUID idVenta) {
        return detalleRepo.findByVenta_Id(idVenta).stream()
                .map(detalleMapper::toResponseDTO)
                .toList();
    }
}
