package com.bikeStore.demo.service.impl;

import com.bikeStore.demo.Entity.*;
import com.bikeStore.demo.Enums.TipoSalida;
import com.bikeStore.demo.dto.request.VentaDtoRequest;
import com.bikeStore.demo.dto.response.VentaDtoResponse;
import com.bikeStore.demo.mapper.VentaMapper;
import com.bikeStore.demo.repository.BicicletaRepository;
import com.bikeStore.demo.repository.SalidaRepository;
import com.bikeStore.demo.repository.UsuarioRepository;
import com.bikeStore.demo.repository.VentaRepository;
import com.bikeStore.demo.service.FacturaEmailService;
import com.bikeStore.demo.service.IVentaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VentaServiceImpl implements IVentaService {

    private final VentaRepository    ventaRepo;
    private final UsuarioRepository  usuarioRepo;
    private final BicicletaRepository biciRepo;
    private final SalidaRepository   salidaRepo;
    private final VentaMapper        ventaMapper;
    private final FacturaEmailService facturaEmailService;

    @Override
    @Transactional
    public VentaDtoResponse registrarVenta(VentaDtoRequest request) {
        // 1. Validar usuario
        UUID usuarioId = request.idUsuario();
        Usuario usuario = usuarioRepo.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + usuarioId));

        Venta venta = ventaMapper.toEntity(request);
        venta.setUsuario(usuario);
        venta.setTotalVenta(BigDecimal.ZERO);
        venta.setDetalles(new ArrayList<>());

        if (venta.getFecha() == null) {
            venta.setFecha(LocalDateTime.now());
        } else {
            if (venta.getFecha().getYear() < 2020) {
                throw new RuntimeException("La fecha de la venta no puede ser anterior al año 2020. Por favor, verificá los datos enviados.");
            }
            if (venta.getFecha().toLocalDate().isAfter(java.time.LocalDate.now())) {
                throw new RuntimeException("Locura cósmica: No podés registrar una venta en el futuro.");
            }
        }

        // 2. Procesar cada detalle
        request.detalles().forEach(detReq -> {
            Bicicleta bici = biciRepo.findById(detReq.idBicicleta())
                    .orElseThrow(() -> new RuntimeException("Bicicleta no encontrada con ID: " + detReq.idBicicleta()));

            // Validar bicicleta activa
            if (!bici.isActivo()) {
                throw new RuntimeException("La bicicleta '" + bici.getCodigo() + "' está inactiva. No se puede vender.");
            }

            // Validar stock suficiente
            if (bici.getStock() < detReq.cantidad()) {
                throw new RuntimeException("Stock insuficiente para '" + bici.getCodigo()
                        + "'. Stock actual: " + bici.getStock()
                        + ", cantidad solicitada: " + detReq.cantidad());
            }

            // Descontar stock
            bici.setStock(bici.getStock() - detReq.cantidad());
            biciRepo.save(bici);

            // Crear detalle de venta
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

        // 3. Guardar venta (cascade guarda los detalles)
        Venta ventaGuardada = ventaRepo.save(venta);

        // 4. Crear registros de Salida (tipo VENTA) por cada detalle
        for (DetalleVenta detalle : ventaGuardada.getDetalles()) {
            Salida salida = new Salida();
            salida.setBicicleta(detalle.getBicicleta());
            salida.setDetalleVenta(detalle);
            salida.setUsuario(usuario);
            salida.setTipoSalida(TipoSalida.VENTA);
            salida.setCantidad(detalle.getCantidad());
            salida.setObservacion("Venta registrada");
            salida.setFecha(ventaGuardada.getFecha());
            salidaRepo.save(salida);
        }

        // Envío de factura por correo (async — no bloquea la respuesta)
        if (request.emailCliente() != null && !request.emailCliente().isBlank()) {
            facturaEmailService.enviarFacturaPorCorreo(
                    ventaGuardada.getId(), request.emailCliente());
        }

        return ventaMapper.toResponseDTO(ventaGuardada);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VentaDtoResponse> listarTodas() {
        return ventaRepo.findAll().stream()
                .map(ventaMapper::toResponseDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public VentaDtoResponse buscarPorId(UUID id) {
        Venta venta = ventaRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada con ID: " + id));
        return ventaMapper.toResponseDTO(venta);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VentaDtoResponse> listarPorUsuario(UUID idUsuario) {
        return ventaRepo.findByUsuarioId(idUsuario).stream()
                .map(ventaMapper::toResponseDTO)
                .toList();
    }
}
