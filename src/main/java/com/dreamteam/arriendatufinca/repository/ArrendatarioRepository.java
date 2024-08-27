package com.dreamteam.arriendatufinca.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.dreamteam.arriendatufinca.entities.Arrendatario;

public interface ArrendatarioRepository extends CrudRepository<Arrendatario, Integer>{
    public Optional<Arrendatario> findByEmail(String email);
} 