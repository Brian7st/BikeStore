package com.bikeStore.demo.service;


import com.bikeStore.demo.Entity.Bicicleta;
import com.bikeStore.demo.Entity.Movimiento;
import com.bikeStore.demo.Entity.Usuario;
import com.bikeStore.demo.Enums.TipoMovimiento;
import com.bikeStore.demo.dto.request.MovimientoDtoResquest;
import com.bikeStore.demo.dto.response.MovimientoDtoResponse;
import com.bikeStore.demo.mapper.MovimientoMapper;
import com.bikeStore.demo.repository.BicicletaRepository;
import com.bikeStore.demo.repository.MovimientoRepository;
import com.bikeStore.demo.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MovimientoService {

    private final MovimientoRepository movimientoRepository;
    private final BicicletaRepository biciletaRepository;
    private final UsuarioRepository usuarioRepository;
    private final MovimientoMapper movimientoMapper;

    //Registrare movimiento (Entrada o salida) tambien actualizar stock
    @Transactional
    public MovimientoDtoResponse registrar(MovimientoDtoResquest dto){
        Bicicleta bicicleta = biciletaRepository.findById(dto.getIdBicicleta())
                .orElseThrow(() -> new RuntimeException("Bicicleta no encontrada con ID: " + dto.getIdBicicleta()));

        Usuario usuario = usuarioRepository.findById(dto.getIdUsuario())
                .orElseThrow(() -> new RuntimeException("usuario no encontrado con ID:" + dto.getIdUsuario()));

        //Actualizar stock segun tipo de movimiento
        if (dto.getTipo() == TipoMovimiento.ENTRADA){
            bicicleta.setStock(bicicleta.getStock() + dto.getCantidad());
        } else if (dto.getTipo() == TipoMovimiento.SALIDA) {
            if (bicicleta.getStock() < dto.getCantidad()) {
                throw new RuntimeException("Stick insuficiente. stock actual: " + bicicleta.getStock());
            }
            bicicleta.setStock(bicicleta.getStock() - dto.getCantidad());
        }

        biciletaRepository.save(bicicleta);

        // Crear y guardar el movimiento
        Movimiento movimiento = new Movimiento();
        movimiento.setBicicleta(bicicleta);
        movimiento.setUsuario(usuario);
        movimiento.setTipo(dto.getTipo());
        movimiento.setCantidad(dto.getCantidad());
        movimiento.setFecha(LocalDateTime.now());

        return movimientoMapper.toResponseDto(movimientoRepository.save(movimiento));
    }

    //Listar todos los movimientos
    public List<MovimientoDtoResponse> listarTodos(){
        return movimientoRepository.findAll()
                .stream()
                .map(movimientoMapper::toResponseDto)
                .toList();
    }

    // Buscar movimiento por ID
    public MovimientoDtoResponse buscarPorId(Integer id){
        Movimiento movimiento = movimientoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movimiento no encontrado por ID: " + id));
        return movimientoMapper.toResponseDto(movimiento);
    }

}
