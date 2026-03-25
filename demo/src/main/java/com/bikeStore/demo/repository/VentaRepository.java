package com.bikeStore.demo.repository;

import com.bikeStore.demo.Entity.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface VentaRepository extends JpaRepository<Venta, UUID> {
    List<Venta> findByFecha(LocalDateTime fecha);

    List<Venta> findByUsuarioId(UUID idUsuario);

    @Query("SELECT DISTINCT v FROM Venta v JOIN FETCH v.detalles d JOIN FETCH d.bicicleta")
    List<Venta> findAllWithDetalles();

    @Query("SELECT DISTINCT v FROM Venta v JOIN FETCH v.detalles d JOIN FETCH d.bicicleta WHERE v.id = :id")
    Optional<Venta> findByIdWithDetalles(@Param("id") UUID id);
}
