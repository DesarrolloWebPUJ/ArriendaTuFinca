package com.dreamteam.arriendatufinca.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.dreamteam.arriendatufinca.entities.Solicitud;

public interface SolicitudRepository extends CrudRepository<Solicitud, Integer> {
    @Query("SELECT s FROM Solicitud s WHERE s.propiedad.arrendador.idCuenta = :arrendadorId ORDER BY s.fechaCreacion DESC")
    List<Solicitud> findTopByFechaCreacionDesc(@Param("arrendadorId") Integer arrendadorId, Pageable pageable);

    @Query("SELECT s FROM Solicitud s WHERE s.propiedad.arrendador.idCuenta = :arrendadorId ORDER BY s.fechaCreacion DESC")
    List<Solicitud> findByArrendadorId(@Param("arrendadorId") Integer arrendadorId);

    @Query("SELECT s FROM Solicitud s WHERE s.arrendatario.idCuenta = :arrendatarioId")
    List<Solicitud> findByArrendatarioId(@Param("arrendatarioId") Integer arrendatarioId);
}
