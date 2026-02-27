package com.bikeStore.demo.Entity;


import com.bikeStore.demo.Enums.Tipo;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Setter @Getter
@RequiredArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Bicicleta")
public class Bicicleta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_bicicleta")
    private  Integer id;

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
