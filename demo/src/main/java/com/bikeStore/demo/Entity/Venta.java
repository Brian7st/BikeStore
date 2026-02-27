package com.bikeStore.demo.Entity;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.math.BigDecimal;
@Entity
@Table(name= "Venta")
@Data
public class Venta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "id_venta")
    private Long idVenta;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @Column(name = "fecha")
    private LocalDateTime fecha;

    @Column(name = "total_venta",
    precision = 10, scale = 0)
    private BigDecimal totalVenta;

    @OneToMany(mappedBy = "venta",
    cascade = CascadeType.ALL)
    private List<DetalleVenta> detalles;

}
