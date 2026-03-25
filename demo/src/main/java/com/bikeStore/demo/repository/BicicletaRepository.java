package com.bikeStore.demo.repository;

import com.bikeStore.demo.Entity.Bicicleta;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface BicicletaRepository extends JpaRepository<Bicicleta, UUID> {

    Optional<Bicicleta> findByCodigo(String codigo);
    List<Bicicleta>findByActivoTrue();

    boolean existsByCodigo(String codigo);
}
