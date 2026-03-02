package com.bikeStore.demo.Entity;

import jakarta.persistence.*;

@Entity  // le dice a JPA QUE ES UNA TABLA
@Table(name = "usuario")
public class Usuario {

    @Id // CLAVE PRIMARIA
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ES EL AUTO INCREMENTABLE
    @Column

    private Integer IdUsuario;

    private String usuario;

    @Column(unique = true)
    private String document;

    private String telefono;

}
