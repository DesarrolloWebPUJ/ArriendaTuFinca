package com.dreamteam.arriendatufinca.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.dreamteam.arriendatufinca.entities.Cuenta;

public interface CuentaRepository extends CrudRepository<Cuenta, Integer>{
    //JPQL
    @Query("SELECT c FROM Cuenta c WHERE c.nombreCuenta = :nombre_cuenta")
    public Optional<Cuenta> findByNombreCuenta(String nombre);

    //SQL nativo
    @Query(value = "SELECT * FROM Cuenta c WHERE c.nombreCuenta = :nombre_cuenta", nativeQuery = true)
    public Optional<Cuenta> findByNombreNative(String nombre);

    public Optional<Cuenta> findByEmail(String email);
}
