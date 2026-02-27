package com.bikeStore.demo.service;


import com.bikeStore.demo.Entity.Bicicleta;
import com.bikeStore.demo.dto.request.BicicletaDtoRequest;
import com.bikeStore.demo.dto.request.BicicletaUpdateDto;
import com.bikeStore.demo.dto.response.BicicletaDtoResponse;
import com.bikeStore.demo.mapper.BicicletaMapper;
import com.bikeStore.demo.repository.BicicletaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BicicletaService {
    private final BicicletaRepository bicicletaRepository;
    private final BicicletaMapper bicicletaMapper;


    @Transactional
    public BicicletaDtoResponse crearBicicleta(BicicletaDtoRequest dto){

        if(bicicletaRepository.findByCodigo(dto.codigo())){
            throw  new RuntimeException("El codigo " + dto.codigo() + " ya esta en uso");
        }

        Bicicleta bicicleta = bicicletaMapper.toEntity(dto);
        Bicicleta guardar = bicicletaRepository.save(bicicleta);
        return  bicicletaMapper.toResponseDto(guardar);
    }

    @Transactional(readOnly = true)
    public BicicletaDtoResponse buscarId(Integer id){
        return bicicletaRepository.findById(id)
                .map(bicicletaMapper::toResponseDto)
                .orElseThrow(() -> new RuntimeException("No se encontro una bicicleta con el id " + id));
    }

    @Transactional(readOnly = true)
    public List<BicicletaDtoResponse> listarTodo(){
        return bicicletaRepository.findActivoTrue()
                .stream().map(bicicletaMapper::toResponseDto)
                .toList();
    }

    @Transactional
    public  BicicletaDtoResponse actualizarBicicleta(Integer id, BicicletaUpdateDto dto){

        Bicicleta bicicletaExistente = bicicletaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No se encontró la bicicleta con ID: " + id));

        bicicletaMapper.updateEntityFromDto(dto,bicicletaExistente);

        Bicicleta actualizada = bicicletaRepository.save(bicicletaExistente);

        return bicicletaMapper.toResponseDto(actualizada);
    }

    @Transactional
    public void eliminarBicicleta(Integer id){
        Bicicleta bicicleta = bicicletaRepository.findById(id).orElseThrow(() -> new RuntimeException("No se puede eliminar: Bicicleta no encontrada por el id " + id));
        bicicleta.setActivo(false);
        bicicletaRepository.save(bicicleta);
    }

}
