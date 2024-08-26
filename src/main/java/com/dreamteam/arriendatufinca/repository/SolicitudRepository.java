package com.dreamteam.arriendatufinca.repository;

import com.dreamteam.arriendatufinca.entities.Solicitud;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SolicitudRepository extends JpaRepository<Solicitud, Long> {
}
