package com.dreamteam.arriendatufinca.repository;

import com.dreamteam.arriendatufinca.entities.Solicitud;
import com.dreamteam.arriendatufinca.enums.SolicitudStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class SolicitudRepositoryTest {

    @Autowired
    private SolicitudRepository solicitudRepository;

    private Solicitud solicitud;

    @BeforeEach
    void setUp() {
        solicitud = new Solicitud();
        solicitud.setIdSolicitud(1);
        solicitud.setCantidadPersonas(4);
        solicitud.setEstadoSolicitud(SolicitudStatus.PENDIENTE.getEntity());
        // Agrega más configuraciones de la solicitud si es necesario
    }

    @Test
    public void testSaveSolicitud() {
        Solicitud savedSolicitud = solicitudRepository.save(solicitud);
        assertNotNull(savedSolicitud);
        assertEquals(solicitud.getCantidadPersonas(), savedSolicitud.getCantidadPersonas());
    }

    @Test
    public void testFindById_Success() {
        solicitudRepository.save(solicitud);
        Optional<Solicitud> foundSolicitud = solicitudRepository.findById(solicitud.getIdSolicitud());
        assertTrue(foundSolicitud.isPresent());
        assertEquals(solicitud.getIdSolicitud(), foundSolicitud.get().getIdSolicitud());
    }

    @Test
    public void testFindById_NotFound() {
        Optional<Solicitud> foundSolicitud = solicitudRepository.findById(999);  // ID inexistente
        assertFalse(foundSolicitud.isPresent());
    }

    @Test
    public void testDeleteSolicitud() {
        Solicitud savedSolicitud = solicitudRepository.save(solicitud);
        solicitudRepository.delete(savedSolicitud);
        Optional<Solicitud> foundSolicitud = solicitudRepository.findById(savedSolicitud.getIdSolicitud());
        assertFalse(foundSolicitud.isPresent());
    }
}
