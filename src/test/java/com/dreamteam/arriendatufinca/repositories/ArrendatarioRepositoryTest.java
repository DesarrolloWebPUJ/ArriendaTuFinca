package com.dreamteam.arriendatufinca.repositories;

import java.sql.Date;
import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.dreamteam.arriendatufinca.entities.Arrendatario;
import com.dreamteam.arriendatufinca.entities.Propiedad;
import com.dreamteam.arriendatufinca.entities.Solicitud;
import com.dreamteam.arriendatufinca.enums.Estado;
import com.dreamteam.arriendatufinca.repository.ArrendatarioRepository;
import com.dreamteam.arriendatufinca.repository.PropiedadRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class ArrendatarioRepositoryTest {

    @Autowired
    private ArrendatarioRepository arrendatarioRepository;

    @Autowired
    private PropiedadRepository propiedadRepository;

    @Test
    void testGuardarArrendatario() {

        Arrendatario arrendatario = new Arrendatario("arrendatario1", "contrasena1", "arrendatario1@example.com", "apellido1", "telefono1");

        Propiedad propiedad = new Propiedad();
        propiedad.setNombrePropiedad("Casa en la playa");
        propiedad.setEstado(Estado.ACTIVE);
        propiedadRepository.save(propiedad);

        Solicitud solicitud = new Solicitud();
        solicitud.setPropiedad(propiedad);
        solicitud.setArrendatario(arrendatario);
        solicitud.setFechaInicio(Date.valueOf("2024-10-01").toLocalDate().atStartOfDay());
        solicitud.setFechaFinal(Date.valueOf("2024-10-07").toLocalDate().atStartOfDay());
        solicitud.setCantidadPersonas(4);
        solicitud.setFechaCreacion(Date.valueOf("2024-09-01").toLocalDate().atStartOfDay());

        arrendatario.setSolicitudes(Arrays.asList(solicitud));

        Arrendatario arrendatarioGuardado = arrendatarioRepository.save(arrendatario);

        assertThat(arrendatarioGuardado).isNotNull();
        assertThat(arrendatarioGuardado.getIdCuenta()).isPositive();
        assertThat(arrendatarioGuardado.getSolicitudes()).hasSize(1);
        assertThat(arrendatarioGuardado.getSolicitudes().get(0).getCantidadPersonas()).isEqualTo(4);
    }

    @Test
    void testLeerArrendatario() {
        Arrendatario arrendatario = new Arrendatario("arrendatario2", "contrasena2", "arrendatario2@example.com", "apellido2", "telefono2");

        Propiedad propiedad = new Propiedad();
        propiedad.setNombrePropiedad("Apartamento en la ciudad");
        propiedad.setEstado(Estado.ACTIVE);
        propiedadRepository.save(propiedad);

        Solicitud solicitud = new Solicitud();
        solicitud.setPropiedad(propiedad);
        solicitud.setArrendatario(arrendatario);
        solicitud.setFechaInicio(Date.valueOf("2024-11-01").toLocalDate().atStartOfDay());
        solicitud.setFechaFinal(Date.valueOf("2024-11-05").toLocalDate().atStartOfDay());
        solicitud.setCantidadPersonas(2);
        solicitud.setFechaCreacion(Date.valueOf("2024-09-15").toLocalDate().atStartOfDay());

        arrendatario.setSolicitudes(Arrays.asList(solicitud));
        arrendatarioRepository.save(arrendatario);

        Optional<Arrendatario> arrendatarioEncontrado = arrendatarioRepository.findById(arrendatario.getIdCuenta());

        assertThat(arrendatarioEncontrado).isPresent();
        assertThat(arrendatarioEncontrado.get().getNombreCuenta()).isEqualTo("arrendatario2");
        assertThat(arrendatarioEncontrado.get().getSolicitudes()).hasSize(1);
        assertThat(arrendatarioEncontrado.get().getSolicitudes().get(0).getFechaInicio()).isEqualTo(Date.valueOf("2024-11-01").toLocalDate().atStartOfDay());
    }

    @Test
    void testActualizarArrendatario() {
        Arrendatario arrendatario = new Arrendatario("arrendatario3", "contrasena3", "arrendatario3@example.com", "apellido3", "telefono3");
        arrendatarioRepository.save(arrendatario);

        arrendatario.setContrasena("nuevaContrasena");
        Arrendatario arrendatarioActualizado = arrendatarioRepository.save(arrendatario);

        assertThat(arrendatarioActualizado.getContrasena()).isEqualTo("nuevaContrasena");
    }

    // Prueba para la correcta gestion de solicitudes existentes en mas de una propiedad.

    @Test
    void testRelacionOneToMany() {
        Arrendatario arrendatario = new Arrendatario("arrendatario5", "contrasena5", "arrendatario5@example.com", "apellido5", "telefono5");

        Propiedad propiedad1 = new Propiedad();
        propiedad1.setNombrePropiedad("Casa en la playa");
        propiedad1.setEstado(Estado.ACTIVE);
        propiedadRepository.save(propiedad1);

        Propiedad propiedad2 = new Propiedad();
        propiedad2.setNombrePropiedad("Cabaña en la montaña");
        propiedad2.setEstado(Estado.ACTIVE);
        propiedadRepository.save(propiedad2);

        Solicitud solicitud1 = new Solicitud();
        solicitud1.setPropiedad(propiedad1);
        solicitud1.setArrendatario(arrendatario);
        solicitud1.setFechaInicio(Date.valueOf("2024-12-15").toLocalDate().atStartOfDay());
        solicitud1.setFechaFinal(Date.valueOf("2024-12-20").toLocalDate().atStartOfDay());
        solicitud1.setCantidadPersonas(5);
        solicitud1.setFechaCreacion(Date.valueOf("2024-11-01").toLocalDate().atStartOfDay());

        Solicitud solicitud2 = new Solicitud();
        solicitud2.setPropiedad(propiedad2);
        solicitud2.setArrendatario(arrendatario);
        solicitud2.setFechaInicio(Date.valueOf("2025-01-10").toLocalDate().atStartOfDay());
        solicitud2.setFechaFinal(Date.valueOf("2025-01-15").toLocalDate().atStartOfDay());
        solicitud2.setCantidadPersonas(6);
        solicitud2.setFechaCreacion(Date.valueOf("2024-12-01").toLocalDate().atStartOfDay());

        arrendatario.setSolicitudes(Arrays.asList(solicitud1, solicitud2));

        Arrendatario arrendatarioGuardado = arrendatarioRepository.save(arrendatario);

        assertThat(arrendatarioGuardado.getSolicitudes()).hasSize(2);
        assertThat(arrendatarioGuardado.getSolicitudes().get(0).getCantidadPersonas()).isEqualTo(5);
        assertThat(arrendatarioGuardado.getSolicitudes().get(1).getCantidadPersonas()).isEqualTo(6);
    }
}

