package com.bikeStore.demo.service.impl;

import com.bikeStore.demo.Entity.Bicicleta;
import com.bikeStore.demo.Entity.Movimiento;
import com.bikeStore.demo.Entity.Usuario;
import com.bikeStore.demo.Entity.Venta;
import com.bikeStore.demo.Enums.TipoMovimiento;
import com.bikeStore.demo.dto.request.MovimientoDtoResquest;
import com.bikeStore.demo.dto.response.MovimientoDtoResponse;
import com.bikeStore.demo.mapper.MovimientoMapper;
import com.bikeStore.demo.repository.BicicletaRepository;
import com.bikeStore.demo.repository.MovimientoRepository;
import com.bikeStore.demo.repository.UsuarioRepository;
import com.bikeStore.demo.service.IMovimientoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MovimientoServiceImpl implements IMovimientoService {

    private final MovimientoRepository movimientoRepository;
    private final BicicletaRepository biciletaRepository;
    private final UsuarioRepository usuarioRepository;
    private final MovimientoMapper movimientoMapper;

    /**
     * Registra un movimiento manual de ENTRADA.
     * Las SALIDAS se generan exclusivamente desde VentaServiceImpl.
     * Si se intenta registrar una SALIDA manual, se lanza una excepcion.
     */
    @Override
    @Transactional
    public MovimientoDtoResponse registrar(MovimientoDtoResquest dto) {

        // Bloquear SALIDA manual: solo se permite desde una Venta
        if (dto.getTipo() == TipoMovimiento.SALIDA) {
            throw new RuntimeException(
                "Las salidas de stock se registran automaticamente al crear una Venta. " +
                "No se permite registrar una SALIDA de forma manual."
            );
        }

        Bicicleta bicicleta = biciletaRepository.findById(dto.getIdBicicleta())
                .orElseThrow(() -> new RuntimeException("Bicicleta no encontrada con ID: " + dto.getIdBicicleta()));

        Usuario usuario = usuarioRepository.findById(dto.getIdUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + dto.getIdUsuario()));

        // Solo ENTRADA: incrementar stock
        bicicleta.setStock(bicicleta.getStock() + dto.getCantidad());
        biciletaRepository.save(bicicleta);

        Movimiento movimiento = new Movimiento();
        movimiento.setBicicleta(bicicleta);
        movimiento.setUsuario(usuario);
        movimiento.setTipo(TipoMovimiento.ENTRADA);
        movimiento.setCantidad(dto.getCantidad());
        movimiento.setFecha(LocalDateTime.now());
        movimiento.setVenta(null); // ENTRADA nunca tiene venta asociada

        return movimientoMapper.toResponseDto(movimientoRepository.save(movimiento));
    }

    /**
     * Metodo interno llamado exclusivamente desde VentaServiceImpl.
     * Crea un Movimiento de SALIDA vinculado a la Venta que lo origino.
     * NO modifica el stock (VentaServiceImpl lo gestiona antes de llamar este metodo).
     */
    @Override
    @Transactional
    public void registrarSalida(Bicicleta bicicleta, Usuario usuario, int cantidad, Venta venta) {
        Movimiento movimiento = new Movimiento();
        movimiento.setBicicleta(bicicleta);
        movimiento.setUsuario(usuario);
        movimiento.setTipo(TipoMovimiento.SALIDA);
        movimiento.setCantidad(cantidad);
        movimiento.setFecha(LocalDateTime.now());
        movimiento.setVenta(venta); // Clave: traza la SALIDA a la Venta que la origino
        movimientoRepository.save(movimiento);
    }

    // Listar todos los movimientos
    @Override
    @Transactional(readOnly = true)
    public List<MovimientoDtoResponse> listarTodos() {
        return movimientoRepository.findAll()
                .stream()
                .map(movimientoMapper::toResponseDto)
                .toList();
    }

    // Buscar movimiento por ID
    @Override
    @Transactional(readOnly = true)
    public MovimientoDtoResponse buscarPorId(UUID id) {
        Movimiento movimiento = movimientoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movimiento no encontrado por ID: " + id));
        return movimientoMapper.toResponseDto(movimiento);
    }

}
