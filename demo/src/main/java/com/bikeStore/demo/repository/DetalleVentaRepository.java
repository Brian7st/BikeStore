package com.bikeStore.demo.repository;

import com.bikeStore.demo.Entity.DetalleVenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface DetalleVentaRepository extends JpaRepository<DetalleVenta, UUID> {
    List<DetalleVenta> findByVenta_Id(UUID idVenta);
}
