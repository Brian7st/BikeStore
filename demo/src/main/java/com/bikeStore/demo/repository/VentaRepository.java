package com.bikeStore.demo.repository;

import com.bikeStore.demo.Entity.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface VentaRepository extends JpaRepository<Venta, UUID> {
    List<Venta> findByFecha(LocalDateTime fecha);

    List<Venta> findByUsuarioId(UUID idUsuario);

    List<Venta> findByFechaBetween(LocalDateTime inicio, LocalDateTime fin);
}
