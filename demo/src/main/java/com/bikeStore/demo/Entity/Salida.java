package com.bikeStore.demo.Entity;

import com.bikeStore.demo.Entity.base.BaseEntity;
import com.bikeStore.demo.Enums.TipoSalida;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "salida")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@AttributeOverride(name = "id", column = @Column(name = "id_salida"))
public class Salida extends BaseEntity {

    public UUID getIdSalida() { return super.getId(); }
    public void setIdSalida(UUID id) { super.setId(id); }

    @ManyToOne
    @JoinColumn(name = "id_bicicleta", nullable = false)
    private Bicicleta bicicleta;

    @ManyToOne
    @JoinColumn(name = "id_detalle_venta")
    private DetalleVenta detalleVenta;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_salida", nullable = false)
    private TipoSalida tipoSalida;

    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;

    @Column(name = "observacion", length = 255)
    private String observacion;

    @Column(name = "fecha", nullable = false)
    private LocalDateTime fecha;
}
