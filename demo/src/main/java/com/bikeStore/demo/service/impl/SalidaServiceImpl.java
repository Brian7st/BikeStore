package com.bikeStore.demo.service.impl;

import com.bikeStore.demo.Entity.Bicicleta;
import com.bikeStore.demo.Entity.Salida;
import com.bikeStore.demo.Entity.Usuario;
import com.bikeStore.demo.Enums.TipoSalida;
import com.bikeStore.demo.dto.request.SalidaBajaDtoRequest;
import com.bikeStore.demo.dto.response.SalidaDtoResponse;
import com.bikeStore.demo.mapper.SalidaMapper;
import com.bikeStore.demo.repository.BicicletaRepository;
import com.bikeStore.demo.repository.SalidaRepository;
import com.bikeStore.demo.repository.UsuarioRepository;
import com.bikeStore.demo.service.ISalidaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SalidaServiceImpl implements ISalidaService {

    private final SalidaRepository salidaRepository;
    private final BicicletaRepository bicicletaRepository;
    private final UsuarioRepository usuarioRepository;
    private final SalidaMapper salidaMapper;

    @Override
    @Transactional
    public SalidaDtoResponse registrarBaja(SalidaBajaDtoRequest dto) {
        Bicicleta bicicleta = bicicletaRepository.findById(dto.idBicicleta())
                .orElseThrow(() -> new RuntimeException("Bicicleta no encontrada con ID: " + dto.idBicicleta()));

        Usuario usuario = usuarioRepository.findById(dto.idUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + dto.idUsuario()));

        if (bicicleta.getStock() < dto.cantidad()) {
            throw new RuntimeException("Stock insuficiente. Stock actual: " + bicicleta.getStock()
                    + ", cantidad solicitada: " + dto.cantidad());
        }

        // Descontar stock
        bicicleta.setStock(bicicleta.getStock() - dto.cantidad());
        bicicletaRepository.save(bicicleta);

        // Crear registro de salida por baja
        Salida salida = new Salida();
        salida.setBicicleta(bicicleta);
        salida.setUsuario(usuario);
        salida.setTipoSalida(TipoSalida.BAJA);
        salida.setCantidad(dto.cantidad());
        salida.setObservacion(dto.observacion());
        salida.setFecha(LocalDateTime.now());
        // detalleVenta queda null en baja

        return salidaMapper.toResponseDto(salidaRepository.save(salida));
    }

    @Override
    @Transactional(readOnly = true)
    public List<SalidaDtoResponse> listarTodas() {
        return salidaRepository.findAll().stream()
                .map(salidaMapper::toResponseDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public SalidaDtoResponse buscarPorId(UUID id) {
        Salida salida = salidaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Salida no encontrada con ID: " + id));
        return salidaMapper.toResponseDto(salida);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SalidaDtoResponse> listarPorVenta(UUID idVenta) {
        return salidaRepository.findByDetalleVenta_Venta_Id(idVenta).stream()
                .map(salidaMapper::toResponseDto)
                .toList();
    }
}
