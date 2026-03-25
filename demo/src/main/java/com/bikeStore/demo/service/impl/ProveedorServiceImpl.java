package com.bikeStore.demo.service.impl;

import com.bikeStore.demo.Entity.Proveedor;
import com.bikeStore.demo.dto.request.ProveedorDtoRequest;
import com.bikeStore.demo.dto.response.ProveedorDtoResponse;
import com.bikeStore.demo.mapper.ProveedorMapper;
import com.bikeStore.demo.repository.ProveedorRepository;
import com.bikeStore.demo.service.IProveedorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProveedorServiceImpl implements IProveedorService {

    private final ProveedorRepository proveedorRepository;
    private final ProveedorMapper proveedorMapper;

    @Override
    @Transactional
    public ProveedorDtoResponse crear(ProveedorDtoRequest dto) {
        if (proveedorRepository.existsByNombre(dto.nombre())) {
            throw new RuntimeException("Ya existe un proveedor con el nombre: " + dto.nombre());
        }
        Proveedor proveedor = proveedorMapper.toEntity(dto);
        return proveedorMapper.toResponseDto(proveedorRepository.save(proveedor));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProveedorDtoResponse> listarTodos() {
        return proveedorRepository.findAll().stream()
                .map(proveedorMapper::toResponseDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ProveedorDtoResponse buscarPorId(UUID id) {
        Proveedor proveedor = proveedorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado con ID: " + id));
        return proveedorMapper.toResponseDto(proveedor);
    }

    @Override
    @Transactional
    public ProveedorDtoResponse actualizar(UUID id, ProveedorDtoRequest dto) {
        Proveedor proveedor = proveedorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado con ID: " + id));

        if (!proveedor.getNombre().equals(dto.nombre()) && proveedorRepository.existsByNombre(dto.nombre())) {
            throw new RuntimeException("Ya existe un proveedor con el nombre: " + dto.nombre());
        }

        proveedorMapper.updateFromDto(dto, proveedor);
        return proveedorMapper.toResponseDto(proveedorRepository.save(proveedor));
    }

    @Override
    @Transactional
    public ProveedorDtoResponse cambiarEstado(UUID id, boolean activo) {
        Proveedor proveedor = proveedorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado con ID: " + id));
        proveedor.setActivo(activo);
        return proveedorMapper.toResponseDto(proveedorRepository.save(proveedor));
    }
}
