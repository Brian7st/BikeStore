package com.bikeStore.demo.Entity;


import com.bikeStore.demo.Enums.Tipo;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

import com.bikeStore.demo.Entity.base.BaseEntity;

@Setter @Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@AttributeOverride(name = "id", column = @Column(name = "id_bicicleta"))
public class Bicicleta extends BaseEntity {

    public UUID getIdBicicleta() { return super.getId(); }
    public void setIdBicicleta(UUID id) { super.setId(id); }


    @Column(name = "codigo", length = 30, nullable = false, unique = true)
    private  String codigo;

    @Column(name = "marca", length = 30)
    private  String marca;

    @Column(name = "modelo", length = 30)
    private  String modelo;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", length = 20)
    private Tipo tipo;

    @Column(name = "stock")
    private  int stock;

    @Column(name = "stock_minimo")
    private  int stockMinimo;

    @Column(name = "valor_unitario", precision = 12, scale = 2)
    private BigDecimal valorUnitario;

    @Column(name = "activo")
    private boolean activo = true;

}
