package com.dreamteam.arriendatufinca.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.dreamteam.arriendatufinca.entities.Calificacion;

public interface CalificacionRepository extends CrudRepository<Calificacion, Integer> {
    @Query("SELECT c FROM Calificacion c WHERE c.calificado.idCuenta = :id_calificado AND (c.tipoCalificacion = 0 OR c.tipoCalificacion = 1)")
    List<Calificacion> findByIdCalificado(@Param("id_calificado") Integer id);
    

}
