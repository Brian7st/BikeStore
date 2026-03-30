package com.bikeStore.demo.service.impl;

import com.bikeStore.demo.Entity.Bicicleta;
import com.bikeStore.demo.dto.request.BicicletaDtoRequest;
import com.bikeStore.demo.dto.request.BicicletaUpdateDto;
import com.bikeStore.demo.dto.response.BicicletaDtoResponse;
import com.bikeStore.demo.mapper.BicicletaMapper;
import com.bikeStore.demo.repository.BicicletaRepository;
import com.bikeStore.demo.service.IBicicletaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BicicletaServiceImpl implements IBicicletaService {
    private final BicicletaRepository bicicletaRepository;
    private final BicicletaMapper bicicletaMapper;

    @Override
    @Transactional
    public BicicletaDtoResponse crearBicicleta(BicicletaDtoRequest dto) {
        if (bicicletaRepository.existsByCodigo(dto.codigo())) {
            throw new RuntimeException("El codigo " + dto.codigo() + " ya esta en uso");
        }
        Bicicleta bicicleta = bicicletaMapper.toEntity(dto);
        Bicicleta guardar = bicicletaRepository.save(bicicleta);
        return bicicletaMapper.toResponseDto(guardar);
    }

    @Override
    @Transactional(readOnly = true)
    public BicicletaDtoResponse buscarId(UUID id) {
        return bicicletaRepository.findById(id)
                .map(bicicletaMapper::toResponseDto)
                .orElseThrow(() -> new RuntimeException("No se encontro una bicicleta con el id " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<BicicletaDtoResponse> listarTodo() {
        return bicicletaRepository.findByActivoTrue()
                .stream().map(bicicletaMapper::toResponseDto)
                .toList();
    }

    @Override
    @Transactional
    public BicicletaDtoResponse actualizarBicicleta(UUID id, BicicletaUpdateDto dto) {
        Bicicleta bicicletaExistente = bicicletaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No se encontró la bicicleta con ID: " + id));

        bicicletaMapper.updateEntityFromDto(dto, bicicletaExistente);
        Bicicleta actualizada = bicicletaRepository.save(bicicletaExistente);
        return bicicletaMapper.toResponseDto(actualizada);
    }

    @Override
    @Transactional
    public void eliminarBicicleta(UUID id) {
        Bicicleta bicicleta = bicicletaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No se puede eliminar: Bicicleta no encontrada por el id " + id));

        // Validar que el stock sea 0 antes de dar de baja
        if (bicicleta.getStock() > 0) {
            throw new RuntimeException(
                    "No se puede dar de baja la bicicleta '" + bicicleta.getCodigo()
                            + "' porque tiene stock de " + bicicleta.getStock()
                            + " unidades. Registre una salida por baja primero.");
        }

        bicicleta.setActivo(false);
        bicicletaRepository.save(bicicleta);
    }
}
