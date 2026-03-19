package com.bikeStore.demo.service.impl;

import com.bikeStore.demo.dto.request.VentaDtoRequest;
import com.bikeStore.demo.dto.response.VentaDtoResponse;
import com.bikeStore.demo.Entity.Bicicleta;
import com.bikeStore.demo.Entity.DetalleVenta;
import com.bikeStore.demo.Entity.Usuario;
import com.bikeStore.demo.Entity.Venta;
import com.bikeStore.demo.mapper.VentaMapper;
import com.bikeStore.demo.repository.BicicletaRepository;
import com.bikeStore.demo.repository.UsuarioRepository;
import com.bikeStore.demo.repository.VentaRepository;
import com.bikeStore.demo.service.IMovimientoService;
import com.bikeStore.demo.service.IVentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.UUID;

@Service
public class VentaServiceImpl implements IVentaService {

    @Autowired private VentaRepository ventaRepo;
    @Autowired private UsuarioRepository usuarioRepo;
    @Autowired private BicicletaRepository biciRepo;
    @Autowired private VentaMapper ventaMapper;
    @Autowired private IMovimientoService movimientoService;

    @Override
    @Transactional
    public VentaDtoResponse registrarVenta(VentaDtoRequest request) {

        // 1. Validar Usuario
        UUID usuarioId = UUID.fromString(request.idUsuario().toString());
        Usuario usuario = usuarioRepo.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Venta venta = ventaMapper.toEntity(request);
        venta.setUsuario(usuario);
        venta.setTotalVenta(BigDecimal.ZERO);
        venta.setDetalles(new ArrayList<>());

        // 2. Procesar Detalles
        request.detalles().forEach(detReq -> {
            UUID biciId = UUID.fromString(detReq.idBicicleta().toString());

            Bicicleta bici = biciRepo.findById(biciId)
                    .orElseThrow(() -> new RuntimeException("Bicicleta no encontrada"));

            // Validar stock suficiente antes de proceder
            if (bici.getStock() < detReq.cantidad()) {
                throw new RuntimeException(
                    "Stock insuficiente para la bicicleta '" + bici.getCodigo() +
                    "'. Stock actual: " + bici.getStock() +
                    ", cantidad solicitada: " + detReq.cantidad()
                );
            }

            DetalleVenta detalle = new DetalleVenta();
            detalle.setVenta(venta);
            detalle.setBicicleta(bici);
            detalle.setCantidad(detReq.cantidad());
            detalle.setPrecioUnitario(bici.getValorUnitario());

            BigDecimal subtotal = detalle.getPrecioUnitario().multiply(new BigDecimal(detalle.getCantidad()));
            detalle.setTotalDetalle(subtotal);

            // Descontar stock
            bici.setStock(bici.getStock() - detReq.cantidad());
            biciRepo.save(bici);

            venta.getDetalles().add(detalle);
            venta.setTotalVenta(venta.getTotalVenta().add(subtotal));
        });

        // 3. Guardar la Venta (cascade guarda los DetalleVenta)
        Venta ventaGuardada = ventaRepo.save(venta);

        // 4. Registrar un Movimiento de SALIDA por cada bicicleta vendida
        //    Se hace DESPUES de guardar la venta para tener el ID de venta disponible
        ventaGuardada.getDetalles().forEach(detalle ->
            movimientoService.registrarSalida(
                detalle.getBicicleta(),
                usuario,
                detalle.getCantidad(),
                ventaGuardada
            )
        );

        return ventaMapper.toResponseDTO(ventaGuardada);
    }
}
