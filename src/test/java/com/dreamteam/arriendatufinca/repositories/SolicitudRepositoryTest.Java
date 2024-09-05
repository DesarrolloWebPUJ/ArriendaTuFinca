package com.dreamteam.arriendatufinca.repositories;

import com.dreamteam.arriendatufinca.entities.EstadoSolicitud;
import com.dreamteam.arriendatufinca.entities.Solicitud;
import com.dreamteam.arriendatufinca.enums.SolicitudStatus;
import com.dreamteam.arriendatufinca.repository.SolicitudRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class SolicitudRepositoryTest {

    @Autowired
    private SolicitudRepository solicitudRepository;

    private Solicitud solicitud;

    @BeforeEach
    void setUp() {
        solicitud = new Solicitud();
        solicitud.setIdSolicitud(1);
        solicitud.setCantidadPersonas(4);
        SolicitudStatus solicitudStatus = SolicitudStatus.PENDIENTE;
        EstadoSolicitud estadoSolicitud = new EstadoSolicitud(solicitudStatus.getId(), solicitudStatus.getNombre());
        solicitud.setEstadoSolicitud(estadoSolicitud);
        // Agrega más configuraciones de la solicitud si es necesario
    }

    @Test
    void testSaveSolicitud() {
        Solicitud savedSolicitud = solicitudRepository.save(solicitud);
        assertNotNull(savedSolicitud);
        assertEquals(solicitud.getCantidadPersonas(), savedSolicitud.getCantidadPersonas());
    }

    @Test
    void testFindById_Success() {
        solicitudRepository.save(solicitud);
        Optional<Solicitud> foundSolicitud = solicitudRepository.findById(solicitud.getIdSolicitud());
        assertTrue(foundSolicitud.isPresent());
        assertEquals(solicitud.getIdSolicitud(), foundSolicitud.get().getIdSolicitud());
    }

    @Test
    void testFindById_NotFound() {
        Optional<Solicitud> foundSolicitud = solicitudRepository.findById(999);  // ID inexistente
        assertFalse(foundSolicitud.isPresent());
    }

}
