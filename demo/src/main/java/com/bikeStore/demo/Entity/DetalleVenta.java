package com.bikeStore.demo.Entity;
import jakarta.persistence.*;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "detalle_venta")
@Data
public class DetalleVenta {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_detalle_venta")
    private UUID idDetalleVenta;

    @ManyToOne
    @JoinColumn(name = "id_venta",
    nullable = false)
    private Venta venta;

    @ManyToOne
    @JoinColumn(name = "id_bicicleta", nullable = false)
    private Bicicleta bicicleta;

    @Column(name = "cantidad")
    private Integer cantidad;

    @Column(name = "precio_unitario",
    precision = 10, scale = 0)
    private BigDecimal precioUnitario;

    @Column(name = "total_detalle",
    precision = 10, scale = 0)
    private BigDecimal totalDetalle;
}
