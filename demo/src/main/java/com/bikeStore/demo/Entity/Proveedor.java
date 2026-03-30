package com.bikeStore.demo.Entity;

import com.bikeStore.demo.Entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "proveedor")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@AttributeOverride(name = "id", column = @Column(name = "id_proveedor"))
public class Proveedor extends BaseEntity {

    public UUID getIdProveedor() { return super.getId(); }
    public void setIdProveedor(UUID id) { super.setId(id); }

    @Column(name = "nombre", length = 100, nullable = false)
    private String nombre;

    @Column(name = "contacto", length = 100)
    private String contacto;

    @Column(name = "telefono", length = 20)
    private String telefono;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "activo", nullable = false)
    private boolean activo = true;

    @OneToMany(mappedBy = "proveedor")
    private List<Entrada> entradas;
}
