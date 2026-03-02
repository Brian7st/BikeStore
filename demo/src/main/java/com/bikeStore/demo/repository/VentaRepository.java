package com.bikeStore.demo.repository;

import com.bikeStore.demo.Entity.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
@Repository
public interface VentaRepository extends JpaRepository<Venta, Long> {
    List<Venta> findByFecha(LocalDateTime fecha);

    List<Venta> findByUsuario_IdUsuario(Long idUsuario);
}
