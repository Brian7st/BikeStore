package com.bikeStore.demo.service.impl;

import com.bikeStore.demo.Entity.Rol;
import com.bikeStore.demo.dto.request.RolDtoRequest;
import com.bikeStore.demo.dto.response.RolDtoResponse;
import com.bikeStore.demo.mapper.RolMapper;
import com.bikeStore.demo.repository.RolRepository;
import com.bikeStore.demo.service.IRolService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RolServiceImpl implements IRolService {

    private final RolRepository rolRepository;
    private final RolMapper rolMapper;

    @Override
    @Transactional
    public RolDtoResponse crear(RolDtoRequest dto) {
        if (rolRepository.existsByNombre(dto.nombre())) {
            throw new RuntimeException("Ya existe el rol: " + dto.nombre());
        }
        Rol rol = rolMapper.toEntity(dto);
        return rolMapper.toResponseDto(rolRepository.save(rol));
    }

    @Override
    @Transactional(readOnly = true)
    public List<RolDtoResponse> listarTodos() {
        return rolRepository.findAll()
                .stream()
                .map(rolMapper::toResponseDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public RolDtoResponse buscarPorId(UUID id) {
        Rol rol = rolRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado con ID: " + id));
        return rolMapper.toResponseDto(rol);
    }

    @Override
    @Transactional(readOnly = true)
    public RolDtoResponse buscarPorNombre(String nombre) {
        Rol rol = rolRepository.findByNombre(nombre)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado: " + nombre));
        return rolMapper.toResponseDto(rol);
    }

    @Override
    @Transactional
    public RolDtoResponse actualizar(UUID id, RolDtoRequest dto) {
        Rol rol = rolRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado con ID: " + id));

        if (!rol.getNombre().equals(dto.nombre()) && rolRepository.existsByNombre(dto.nombre())) {
            throw new RuntimeException("Ya existe el rol: " + dto.nombre());
        }

        rolMapper.updateFromDto(dto, rol);
        return rolMapper.toResponseDto(rolRepository.save(rol));
    }

    @Override
    @Transactional
    public RolDtoResponse cambiarEstado(UUID id, boolean activo) {
        Rol rol = rolRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado con ID: " + id));

        if (!activo) {
            long usuariosActivos = rolRepository.countByNombreAndUsuariosActivo(rol.getNombre(), true);
            if (usuariosActivos > 0) {
                throw new RuntimeException(
                        "No se puede desactivar el rol '" + rol.getNombre() +
                                "' porque tiene " + usuariosActivos + " usuario(s) activo(s)");
            }
        }

        rol.setActivo(activo);
        return rolMapper.toResponseDto(rolRepository.save(rol));
    }
}
