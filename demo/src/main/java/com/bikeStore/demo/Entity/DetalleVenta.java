package com.bikeStore.demo.Entity;
import jakarta.persistence.*;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

import com.bikeStore.demo.Entity.base.BaseEntity;

@Entity
@Table(name = "detalle_venta")
@Data
@AttributeOverride(name = "id", column = @Column(name = "id_detalle_venta"))
public class DetalleVenta extends BaseEntity {

    public UUID getIdDetalleVenta() { return super.getId(); }
    public void setIdDetalleVenta(UUID id) { super.setId(id); }

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
