package com.bikeStore.demo.Entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

import com.bikeStore.demo.Entity.base.BaseEntity;

@Entity
@Table(name = "rol")
@Data
@NoArgsConstructor
@AllArgsConstructor
@AttributeOverride(name = "id", column = @Column(name = "id_rol"))
public class Rol extends BaseEntity {

    public UUID getIdRol() { return super.getId(); }
    public void setIdRol(UUID id) { super.setId(id); }

    //solo admin y empleado
    @Column(name = "nombre", length = 30, nullable = false, unique = true)
    private String nombre;

    @Column(name = "descripcion", length = 100)
    private String descripcion;

    @Column(name = "activo", nullable = false)
    private boolean activo = true;

    @OneToMany(mappedBy = "rol")
    private List<Usuario> usuarios;

}
