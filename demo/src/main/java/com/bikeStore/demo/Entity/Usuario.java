package com.bikeStore.demo.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

import com.bikeStore.demo.Entity.base.BaseEntity;

@Entity  // le dice a JPA QUE ES UNA TABLA
@Table(name = "usuario")
@Data
@NoArgsConstructor
@AllArgsConstructor
@AttributeOverride(name = "id", column = @Column(name = "id_usuario"))
public class Usuario extends BaseEntity {

    public UUID getIdUsuario() { return super.getId(); }
    public void setIdUsuario(UUID id) { super.setId(id); }


    @Column ( name = "Usuario", length = 50, nullable = false)
    private String userName;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(unique = true, nullable = false, length = 20)
    private String document;

    @Column( name = "telefono", length = 30)
    private String telefono;

    @Column(name = "activo", nullable = false)
    private boolean activo = true;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_rol",nullable = false)
    private Rol rol;

}
