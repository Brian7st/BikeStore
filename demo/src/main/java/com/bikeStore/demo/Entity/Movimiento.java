package com.bikeStore.demo.Entity;


import com.bikeStore.demo.Enums.TipoMovimiento;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

import com.bikeStore.demo.Entity.base.BaseEntity;

@Entity
@Table (name = "Movimiento")
@Data
@NoArgsConstructor
@AllArgsConstructor
@AttributeOverride(name = "id", column = @Column(name = "id_movimiento"))
public class Movimiento extends BaseEntity {

    public UUID getIdMovimiento() { return super.getId(); }
    public void setIdMovimiento(UUID id) { super.setId(id); }

    @ManyToOne
    @JoinColumn(name = "id_bicicleta", nullable = false)
    private Bicicleta bicicleta;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoMovimiento tipo;

    @Column(name = "cantidad", length = 30)
    private Integer cantidad;

    @Column(name = "fecha")
    private LocalDateTime fecha;
}
