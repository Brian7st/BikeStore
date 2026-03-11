package com.bikeStore.demo.Entity;


import com.bikeStore.demo.Enums.Tipo;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Setter @Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity

public class Bicicleta {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_bicicleta")
    private UUID id;

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
