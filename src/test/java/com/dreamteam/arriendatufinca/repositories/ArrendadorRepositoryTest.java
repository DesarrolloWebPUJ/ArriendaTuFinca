package com.dreamteam.arriendatufinca.repositories;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.dreamteam.arriendatufinca.entities.Arrendador;
import com.dreamteam.arriendatufinca.entities.Propiedad;
import com.dreamteam.arriendatufinca.enums.Estado;
import com.dreamteam.arriendatufinca.repository.ArrendadorRepository;

@DataJpaTest
public class ArrendadorRepositoryTest {

    @Autowired
    private ArrendadorRepository arrendadorRepository;

    @Test
    public void testGuardarArrendador() {

        Arrendador arrendador = new Arrendador("arrendador1", "contrasena1", "arrendador1@example.com");

        Propiedad propiedad1 = new Propiedad();
        propiedad1.setNombrePropiedad("Casa en la playa");
        propiedad1.setArrendador(arrendador);
        propiedad1.setEstado(Estado.ACTIVE);

        Propiedad propiedad2 = new Propiedad();
        propiedad2.setNombrePropiedad("Cabaña en la montaña");
        propiedad2.setArrendador(arrendador);
        propiedad2.setEstado(Estado.ACTIVE);

        arrendador.setPropiedades(Arrays.asList(propiedad1, propiedad2));

        Arrendador arrendadorGuardado = arrendadorRepository.save(arrendador);

        assertThat(arrendadorGuardado).isNotNull();

        assertThat(arrendadorGuardado.getIdCuenta()).isGreaterThan(0);
        assertThat(arrendadorGuardado.getNombreCuenta()).isEqualTo("arrendador1");
        assertThat(arrendadorGuardado.getContrasena()).isEqualTo("contrasena1");

        assertThat(arrendadorGuardado.getPropiedades()).hasSize(2);
        assertThat(arrendadorGuardado.getPropiedades().get(0).getNombrePropiedad()).isEqualTo("Casa en la playa");
        assertThat(arrendadorGuardado.getPropiedades().get(1).getNombrePropiedad()).isEqualTo("Cabaña en la montaña");
    }

    @Test
    public void testLeerArrendador() {
        Arrendador arrendador = new Arrendador("arrendador2", "contrasena2", "arrendador2@example.com");

        Propiedad propiedad = new Propiedad();
        propiedad.setNombrePropiedad("Apartamento en la ciudad");
        propiedad.setArrendador(arrendador);
        propiedad.setEstado(Estado.ACTIVE);
        arrendador.setPropiedades(Arrays.asList(propiedad));
        arrendadorRepository.save(arrendador);

        Optional<Arrendador> arrendadorEncontrado = arrendadorRepository.findById(arrendador.getIdCuenta());

        assertThat(arrendadorEncontrado).isPresent();
        assertThat(arrendadorEncontrado.get().getNombreCuenta()).isEqualTo("arrendador2");
        assertThat(arrendadorEncontrado.get().getPropiedades()).hasSize(1);
        assertThat(arrendadorEncontrado.get().getPropiedades().get(0).getNombrePropiedad()).isEqualTo("Apartamento en la ciudad");
    }

    @Test
    public void testActualizarArrendador() {
        Arrendador arrendador = new Arrendador("arrendador3", "contrasena3", "arrendador3@example.com");
        arrendadorRepository.save(arrendador);

        arrendador.setContrasena("nuevaContrasena");
        Arrendador arrendadorActualizado = arrendadorRepository.save(arrendador);

        assertThat(arrendadorActualizado.getContrasena()).isEqualTo("nuevaContrasena");
    }

    @Test
    public void testEliminarArrendador() {
        Arrendador arrendador = new Arrendador("arrendador4", "contrasena4", "arrendador4@example.com");

        Propiedad propiedad = new Propiedad();
        propiedad.setNombrePropiedad("Casa de campo");
        propiedad.setArrendador(arrendador);
        propiedad.setEstado(Estado.ACTIVE);
        arrendador.setPropiedades(Arrays.asList(propiedad));
        arrendadorRepository.save(arrendador);

        arrendadorRepository.deleteById(arrendador.getIdCuenta());

        Optional<Arrendador> arrendadorEliminado = arrendadorRepository.findById(arrendador.getIdCuenta());

        assertThat(arrendadorEliminado).isPresent();
        assertThat(arrendadorEliminado.get().getEstado()).isEqualTo(Estado.INACTIVE);
    }

    // Prueba para la correcta gestion de la relacion existente con la entidad Propiedad
    
    @Test
    public void testRelacionPropiedades() {
        Arrendador arrendador = new Arrendador("arrendador5", "contrasena5", "arrendador5@example.com");

        Propiedad propiedad1 = new Propiedad();
        propiedad1.setNombrePropiedad("Casa en la playa");
        propiedad1.setArrendador(arrendador);
        propiedad1.setEstado(Estado.ACTIVE);

        Propiedad propiedad2 = new Propiedad();
        propiedad2.setNombrePropiedad("Cabaña en la montaña");
        propiedad2.setArrendador(arrendador);
        propiedad2.setEstado(Estado.ACTIVE);

        arrendador.setPropiedades(Arrays.asList(propiedad1, propiedad2));

        Arrendador arrendadorGuardado = arrendadorRepository.save(arrendador);

        assertThat(arrendadorGuardado).isNotNull();
        assertThat(arrendadorGuardado.getPropiedades()).hasSize(2);
        assertThat(arrendadorGuardado.getPropiedades().get(0).getNombrePropiedad()).isEqualTo("Casa en la playa");
        assertThat(arrendadorGuardado.getPropiedades().get(1).getNombrePropiedad()).isEqualTo("Cabaña en la montaña");
    }
}
