package com.bikeStore.demo.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity  // le dice a JPA QUE ES UNA TABLA
@Table(name = "usuario")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    @Id // CLAVE PRIMARIA
    @GeneratedValue(strategy = GenerationType.UUID) // ES EL AUTO INCREMENTABLE
    @Column
    private UUID IdUsuario;

    @Column ( name = "Usuario", length = 50, nullable = false)
    private String usuario;

    @Column(name = "passoword", nullable = false)
    private String password;

    @Column(unique = true, nullable = false, length = 20)
    private String document;

    @Column( name = "telefono", length = 30)
    private String telefono;

    @Column(name = "activo", nullable = false)
    private boolean activo = true;

    @ManyToOne
    @JoinColumn(name = "id_rol",nullable = false)
    private Rol rol;

}
