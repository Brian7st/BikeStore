package com.bikeStore.demo.service.impl;

import com.bikeStore.demo.Entity.Bicicleta;
import com.bikeStore.demo.Entity.Entrada;
import com.bikeStore.demo.Entity.Proveedor;
import com.bikeStore.demo.Entity.Usuario;
import com.bikeStore.demo.dto.request.EntradaDtoRequest;
import com.bikeStore.demo.dto.response.EntradaDtoResponse;
import com.bikeStore.demo.mapper.EntradaMapper;
import com.bikeStore.demo.repository.BicicletaRepository;
import com.bikeStore.demo.repository.EntradaRepository;
import com.bikeStore.demo.repository.ProveedorRepository;
import com.bikeStore.demo.repository.UsuarioRepository;
import com.bikeStore.demo.service.IEntradaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EntradaServiceImpl implements IEntradaService {

    private final EntradaRepository entradaRepository;
    private final BicicletaRepository bicicletaRepository;
    private final ProveedorRepository proveedorRepository;
    private final UsuarioRepository usuarioRepository;
    private final EntradaMapper entradaMapper;

    @Override
    @Transactional
    public List<EntradaDtoResponse> registrarEntrada(EntradaDtoRequest dto) {
        Proveedor proveedor = proveedorRepository.findById(dto.idProveedor())
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado con ID: " + dto.idProveedor()));

        if (!proveedor.isActivo()) {
            throw new RuntimeException("El proveedor '" + proveedor.getNombre() + "' está inactivo");
        }

        Usuario usuario = usuarioRepository.findById(dto.idUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + dto.idUsuario()));

        List<Entrada> entradasGuardadas = new ArrayList<>();
        LocalDateTime ahora = LocalDateTime.now();

        for (var item : dto.items()) {
            Bicicleta bicicleta = bicicletaRepository.findById(item.idBicicleta())
                    .orElseThrow(() -> new RuntimeException("Bicicleta no encontrada con ID: " + item.idBicicleta()));

            if (!bicicleta.isActivo()) {
                throw new RuntimeException("La bicicleta '" + bicicleta.getCodigo() + "' está inactiva. No se puede registrar entrada.");
            }

            // Incrementar stock
            bicicleta.setStock(bicicleta.getStock() + item.cantidad());
            bicicletaRepository.save(bicicleta);

            // Crear registro de entrada
            Entrada entrada = new Entrada();
            entrada.setBicicleta(bicicleta);
            entrada.setProveedor(proveedor);
            entrada.setUsuario(usuario);
            entrada.setCantidad(item.cantidad());
            entrada.setFecha(ahora);

            entradasGuardadas.add(entradaRepository.save(entrada));
        }

        return entradasGuardadas.stream()
                .map(entradaMapper::toResponseDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<EntradaDtoResponse> listarTodas() {
        return entradaRepository.findAll().stream()
                .map(entradaMapper::toResponseDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public EntradaDtoResponse buscarPorId(UUID id) {
        Entrada entrada = entradaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Entrada no encontrada con ID: " + id));
        return entradaMapper.toResponseDto(entrada);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EntradaDtoResponse> listarPorProveedor(UUID idProveedor) {
        return entradaRepository.findByProveedor_Id(idProveedor).stream()
                .map(entradaMapper::toResponseDto)
                .toList();
    }
}
