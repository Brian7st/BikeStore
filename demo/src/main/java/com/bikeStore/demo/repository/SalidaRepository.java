package com.bikeStore.demo.repository;

import com.bikeStore.demo.Entity.Salida;
import com.bikeStore.demo.Enums.TipoSalida;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SalidaRepository extends JpaRepository<Salida, UUID> {
    List<Salida> findByBicicleta_Id(UUID idBicicleta);
    List<Salida> findByTipoSalida(TipoSalida tipo);
    List<Salida> findByDetalleVenta_Venta_Id(UUID idVenta);
}
