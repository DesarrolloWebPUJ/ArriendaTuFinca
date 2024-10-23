package com.dreamteam.arriendatufinca.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.dreamteam.arriendatufinca.entities.Propiedad;

public interface PropiedadRepository extends CrudRepository<Propiedad, Integer> {
    @Query("SELECT s FROM Propiedad s WHERE s.arrendador.idCuenta = :arrendadorId")
    List<Propiedad> findByPropiedadArrendadorId(@Param("arrendadorId") Integer arrendadorId);
}
