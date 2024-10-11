package com.dreamteam.arriendatufinca.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.dreamteam.arriendatufinca.entities.Propiedad;
import com.dreamteam.arriendatufinca.entities.Solicitud;

public interface PropiedadRepository extends CrudRepository<Propiedad, Integer> {
    @Query("SELECT s FROM Solicitud s WHERE s.propiedad.arrendador.id = :arrendadorId")
    List<Solicitud> findByPropiedadArrendadorId(@Param("arrendadorId") Integer arrendadorId);

    @Query("SELECT s FROM Solicitud s ORDER BY s.fechaCreacion DESC")
    List<Solicitud> findTopByFechaCreacionDesc(Pageable pageable);
}
