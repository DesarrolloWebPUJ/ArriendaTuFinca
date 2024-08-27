package com.dreamteam.arriendatufinca.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.dreamteam.arriendatufinca.entities.Arrendador;

public interface ArrendadorRepository extends CrudRepository<Arrendador, Integer>{
    public Optional<Arrendador> findByEmail(String email);
} 
