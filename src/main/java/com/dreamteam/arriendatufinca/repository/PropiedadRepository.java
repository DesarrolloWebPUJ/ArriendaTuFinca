package com.dreamteam.arriendatufinca.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.dreamteam.arriendatufinca.entities.Propiedad;
import com.dreamteam.arriendatufinca.enums.Estado;

public interface PropiedadRepository extends CrudRepository<Propiedad, Integer> {
    @Query("SELECT s FROM Propiedad s WHERE s.arrendador.idCuenta = :arrendadorId")
    List<Propiedad> findByPropiedadArrendadorId(@Param("arrendadorId") Integer arrendadorId);

    @Query("SELECT s FROM Propiedad s WHERE s.estado = :estado AND s.arrendador.idCuenta = :arrendadorId")
    List<Propiedad> findByEstadoPropiedadArrendadorId(Estado estado, Integer arrendadorId);

    @Query("SELECT p FROM Propiedad p WHERE p.estado = :estado")
    List<Propiedad> findAllByEstado(@Param("estado") Estado estado);
}
