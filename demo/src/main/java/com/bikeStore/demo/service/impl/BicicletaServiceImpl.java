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
        // ✅ findByCodigo devuelve Optional<Bicicleta>, usamos isPresent()
        if (bicicletaRepository.findByCodigo(dto.codigo()).isPresent()) {
            throw new RuntimeException("El codigo " + dto.codigo() + " ya esta en uso");
        }
        Bicicleta bicicleta = bicicletaMapper.toEntity(dto);
        return bicicletaMapper.toResponseDto(bicicletaRepository.save(bicicleta));
    }

    @Override
    @Transactional(readOnly = true)
    public BicicletaDtoResponse buscarId(UUID id){
        return bicicletaRepository.findById(id)
                .map(bicicletaMapper::toResponseDto)
                .orElseThrow(() -> new RuntimeException("No se encontro una bicicleta con el id " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<BicicletaDtoResponse> listarTodo(){
        return bicicletaRepository.findActivoTrue()
                .stream().map(bicicletaMapper::toResponseDto)
                .toList();
    }

    @Override
    @Transactional
    public  BicicletaDtoResponse actualizarBicicleta(UUID id, BicicletaUpdateDto dto){

        Bicicleta bicicletaExistente = bicicletaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No se encontró la bicicleta con ID: " + id));

        bicicletaMapper.updateEntityFromDto(dto,bicicletaExistente);

        Bicicleta actualizada = bicicletaRepository.save(bicicletaExistente);

        return bicicletaMapper.toResponseDto(actualizada);
    }

    @Override
    @Transactional
    public void eliminarBicicleta(UUID id) { // ✅ UUID
        Bicicleta bicicleta = bicicletaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bicicleta no encontrada con id: " + id));
        bicicleta.setActivo(false);
        bicicletaRepository.save(bicicleta);
    }
}
