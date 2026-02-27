package com.bikeStore.demo.repository;

import com.bikeStore.demo.Entity.Bicicleta;
import com.bikeStore.demo.Enums.Tipo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BicicletaRepository extends JpaRepository<Bicicleta, Integer> {

    Optional<Bicicleta> findByCodigo(String codigo);
    List<Bicicleta> findByMarca(String marca);
    List<Bicicleta> findByTipo(Tipo tipo);
    List<Bicicleta> findByStockLessThan(int limite);

}
