package com.dreamteam.arriendatufinca.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.dreamteam.arriendatufinca.entities.Foto;

public interface FotoRepository extends CrudRepository<Foto, Integer> {

    @Query("SELECT f FROM Foto f WHERE f.propiedad.idPropiedad = :propiedadId")
    List<Foto> findByPropiedadIdPropiedad(@Param("propiedadId") Integer propiedadId);
}