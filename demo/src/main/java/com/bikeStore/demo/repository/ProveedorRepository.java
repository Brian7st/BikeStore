package com.bikeStore.demo.repository;

import com.bikeStore.demo.Entity.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProveedorRepository extends JpaRepository<Proveedor, UUID> {
    Optional<Proveedor> findByNombre(String nombre);
    boolean existsByNombre(String nombre);
}
