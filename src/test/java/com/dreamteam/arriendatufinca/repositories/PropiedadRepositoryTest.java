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

import com.dreamteam.arriendatufinca.entities.Arrendador;
import com.dreamteam.arriendatufinca.entities.Propiedad;
import com.dreamteam.arriendatufinca.entities.Solicitud;
import com.dreamteam.arriendatufinca.enums.Estado;
import com.dreamteam.arriendatufinca.repository.ArrendadorRepository;
import com.dreamteam.arriendatufinca.repository.PropiedadRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class PropiedadRepositoryTest {

    @Autowired
    private PropiedadRepository propiedadRepository;

    @Autowired
    private ArrendadorRepository arrendadorRepository;

    @Test
    void testGuardarPropiedad() {
        Arrendador arrendador = new Arrendador("arrendador1", "contrasena1", "arrendador1@example.com", "apellido1", "telefono1");
        arrendadorRepository.save(arrendador);

        Propiedad propiedad = new Propiedad();
        propiedad.setNombrePropiedad("Casa en la playa");
        propiedad.setDescripcionPropiedad("Una hermosa casa frente al mar");
        propiedad.setMunicipio("Cartagena");
        propiedad.setDepartamento("Bolívar");
        propiedad.setCantidadHabitaciones(3);
        propiedad.setCantidadBanos(2);
        propiedad.setPermiteMascotas(true);
        propiedad.setTienePiscina(true);
        propiedad.setTieneAsador(true);
        propiedad.setValorNoche(450.0f);
        propiedad.setEstado(Estado.ACTIVE);
        propiedad.setArrendador(arrendador);

        Propiedad propiedadGuardada = propiedadRepository.save(propiedad);

        assertThat(propiedadGuardada).isNotNull();
        assertThat(propiedadGuardada.getIdPropiedad()).isPositive();
        assertThat(propiedadGuardada.getArrendador()).isEqualTo(arrendador);
        assertThat(propiedadGuardada.getNombrePropiedad()).isEqualTo("Casa en la playa");
    }

    @Test
    void testLeerPropiedad() {
        Arrendador arrendador = new Arrendador("arrendador2", "contrasena2", "arrendador2@example.com", "apellido2", "telefono2");
        arrendadorRepository.save(arrendador);

        Propiedad propiedad = new Propiedad();
        propiedad.setNombrePropiedad("Cabaña en la montaña");
        propiedad.setDescripcionPropiedad("Una acogedora cabaña en las montañas");
        propiedad.setMunicipio("Santa Marta");
        propiedad.setDepartamento("Magdalena");
        propiedad.setCantidadHabitaciones(2);
        propiedad.setCantidadBanos(1);
        propiedad.setPermiteMascotas(false);
        propiedad.setTienePiscina(false);
        propiedad.setTieneAsador(true);
        propiedad.setValorNoche(300.0f);
        propiedad.setEstado(Estado.ACTIVE);
        propiedad.setArrendador(arrendador);

        propiedadRepository.save(propiedad);

        Optional<Propiedad> propiedadEncontrada = propiedadRepository.findById(propiedad.getIdPropiedad());

        assertThat(propiedadEncontrada).isPresent();
        assertThat(propiedadEncontrada.get().getNombrePropiedad()).isEqualTo("Cabaña en la montaña");
        assertThat(propiedadEncontrada.get().getArrendador()).isEqualTo(arrendador);
        assertThat(propiedadEncontrada.get().getMunicipio()).isEqualTo("Santa Marta");
    }

    @Test
    void testActualizarPropiedad() {
        Arrendador arrendador = new Arrendador("arrendador3", "contrasena3", "arrendador3@example.com", "apellido3", "telefono3");
        arrendadorRepository.save(arrendador);

        Propiedad propiedad = new Propiedad();
        propiedad.setNombrePropiedad("Casa de campo");
        propiedad.setDescripcionPropiedad("Una hermosa casa de campo");
        propiedad.setMunicipio("Rionegro");
        propiedad.setDepartamento("Antioquia");
        propiedad.setCantidadHabitaciones(4);
        propiedad.setCantidadBanos(3);
        propiedad.setPermiteMascotas(true);
        propiedad.setTienePiscina(true);
        propiedad.setTieneAsador(true);
        propiedad.setValorNoche(500.0f);
        propiedad.setEstado(Estado.ACTIVE);
        propiedad.setArrendador(arrendador);

        propiedadRepository.save(propiedad);

        // Actualizar valor de la noche y descripción
        propiedad.setValorNoche(550.0f);
        propiedad.setDescripcionPropiedad("Una casa de campo renovada");
        Propiedad propiedadActualizada = propiedadRepository.save(propiedad);

        assertThat(propiedadActualizada.getValorNoche()).isEqualTo(550.0f);
        assertThat(propiedadActualizada.getDescripcionPropiedad()).isEqualTo("Una casa de campo renovada");
    }

    @Test
    void testEliminarPropiedad() {
        Arrendador arrendador = new Arrendador("arrendador4", "contrasena4", "arrendador4@example.com", "apellido4", "telefono4");
        arrendadorRepository.save(arrendador);

        Propiedad propiedad = new Propiedad();
        propiedad.setNombrePropiedad("Apartamento en la ciudad");
        propiedad.setDescripcionPropiedad("Moderno apartamento en el centro");
        propiedad.setMunicipio("Bogotá");
        propiedad.setDepartamento("Cundinamarca");
        propiedad.setCantidadHabitaciones(1);
        propiedad.setCantidadBanos(1);
        propiedad.setPermiteMascotas(false);
        propiedad.setTienePiscina(false);
        propiedad.setTieneAsador(false);
        propiedad.setValorNoche(200.0f);
        propiedad.setEstado(Estado.ACTIVE);
        propiedad.setArrendador(arrendador);

        propiedadRepository.save(propiedad);

        propiedadRepository.deleteById(propiedad.getIdPropiedad());

        Optional<Propiedad> propiedadEliminada = propiedadRepository.findById(propiedad.getIdPropiedad());

        assertThat(propiedadEliminada).isEmpty();
    }

    @Test
    void testRelacionOneToManyConSolicitudes() {
        Arrendador arrendador = new Arrendador("arrendador5", "contrasena5", "arrendador5@example.com", "apellido5", "telefono5");
        arrendadorRepository.save(arrendador);

        Propiedad propiedad = new Propiedad();
        propiedad.setNombrePropiedad("Casa en la playa");
        propiedad.setDescripcionPropiedad("Una hermosa casa frente al mar");
        propiedad.setMunicipio("Cartagena");
        propiedad.setDepartamento("Bolívar");
        propiedad.setCantidadHabitaciones(3);
        propiedad.setCantidadBanos(2);
        propiedad.setPermiteMascotas(true);
        propiedad.setTienePiscina(true);
        propiedad.setTieneAsador(true);
        propiedad.setValorNoche(450.0f);
        propiedad.setEstado(Estado.ACTIVE);
        propiedad.setArrendador(arrendador);

        Solicitud solicitud1 = new Solicitud();
        solicitud1.setPropiedad(propiedad);
        solicitud1.setFechaInicio(Date.valueOf("2024-12-01").toLocalDate());
        solicitud1.setFechaFinal(Date.valueOf("2024-12-07").toLocalDate());
        solicitud1.setCantidadPersonas(4);

        Solicitud solicitud2 = new Solicitud();
        solicitud2.setPropiedad(propiedad);
        solicitud2.setFechaInicio(Date.valueOf("2025-01-01").toLocalDate());
        solicitud2.setFechaFinal(Date.valueOf("2025-01-10").toLocalDate());
        solicitud2.setCantidadPersonas(6);

        propiedad.setSolicitudes(Arrays.asList(solicitud1, solicitud2));

        propiedadRepository.save(propiedad);

        Optional<Propiedad> propiedadGuardada = propiedadRepository.findById(propiedad.getIdPropiedad());

        assertThat(propiedadGuardada).isPresent();
        assertThat(propiedadGuardada.get().getSolicitudes()).hasSize(2);
        assertThat(propiedadGuardada.get().getSolicitudes().get(0).getCantidadPersonas()).isEqualTo(4);
        assertThat(propiedadGuardada.get().getSolicitudes().get(1).getCantidadPersonas()).isEqualTo(6);
    }
}
