package com.bikeStore.demo.repository;

import com.bikeStore.demo.Entity.Entrada;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EntradaRepository extends JpaRepository<Entrada, UUID> {
    List<Entrada> findByBicicleta_Id(UUID idBicicleta);
    List<Entrada> findByProveedor_Id(UUID idProveedor);
}
