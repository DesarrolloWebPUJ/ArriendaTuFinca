package com.dreamteam.arriendatufinca.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.dreamteam.arriendatufinca.entities.Cuenta;

public interface CuentaRepository extends CrudRepository<Cuenta, Long>{
    //JPQL
    @Query("SELECT c FROM Cuenta c WHERE c.nombre = :nombre")
    public Optional<Cuenta> findByNombre(String nombre);

    //SQL nativo
    @Query(value = "SELECT * FROM empresa e WHERE e.nombre = :nombre", nativeQuery = true)
    public Optional<Cuenta> findByNombreNative(String nombre);
}
