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
import com.bikeStore.demo.service.IVentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.UUID;

@Service
public class VentaServiceImpl implements IVentaService{
    @Autowired private VentaRepository ventaRepo;
    @Autowired private UsuarioRepository usuarioRepo;
    @Autowired private BicicletaRepository biciRepo;
    @Autowired private VentaMapper ventaMapper;

    @Override
    @Transactional
    public VentaDtoResponse registrarVenta(VentaDtoRequest request) {

        // 1. Validar Usuario (Convertimos el ID del DTO a UUID)
        // Asumiendo que request.getIdUsuario() devuelve un String o un objeto que represente el UUID
        UUID usuarioId = UUID.fromString(request.idUsuario().toString());
        Usuario usuario = usuarioRepo.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Venta venta = ventaMapper.toEntity(request);
        venta.setUsuario(usuario);
        venta.setTotalVenta(BigDecimal.ZERO);
        venta.setDetalles(new ArrayList<>());

        // 2. Procesar Detalles
        request.detalles().forEach(detReq -> {
            // Convertimos el ID de la bicicleta que viene en el detalle a UUID
            UUID biciId = UUID.fromString(detReq.idBicicleta().toString());

            Bicicleta bici = biciRepo.findById(biciId)
                    .orElseThrow(() -> new RuntimeException("Bicicleta no encontrada"));

            DetalleVenta detalle = new DetalleVenta();
            detalle.setVenta(venta);
            detalle.setBicicleta(bici);
            detalle.setCantidad(detReq.cantidad());
            detalle.setPrecioUnitario(bici.getValorUnitario());

            BigDecimal subtotal = detalle.getPrecioUnitario().multiply(new BigDecimal(detalle.getCantidad()));
            detalle.setTotalDetalle(subtotal);

            venta.getDetalles().add(detalle);
            venta.setTotalVenta(venta.getTotalVenta().add(subtotal));
        });

        Venta ventaGuardada = ventaRepo.save(venta);
        return ventaMapper.toResponseDTO(ventaGuardada);
    }
}
