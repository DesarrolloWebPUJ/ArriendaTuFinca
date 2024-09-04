package com.dreamteam.arriendatufinca.repositories;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.dreamteam.arriendatufinca.entities.Cuenta;
import com.dreamteam.arriendatufinca.enums.Estado;
import com.dreamteam.arriendatufinca.repository.CuentaRepository;

@DataJpaTest
public class CuentaRepositoryTest {

    @Autowired
    private CuentaRepository cuentaRepository;

    @Test
    public void testGuardarCuenta() {
        Cuenta cuenta = new Cuenta("usuario1", "contrasena1", "usuario1@example.com");
        Cuenta cuentaGuardada = cuentaRepository.save(cuenta);

        assertThat(cuentaGuardada).isNotNull();
        assertThat(cuentaGuardada.getIdCuenta()).isGreaterThan(0);
        assertThat(cuentaGuardada.getNombreCuenta()).isEqualTo("usuario1");
        assertThat(cuentaGuardada.getContrasena()).isEqualTo("contrasena1");
        assertThat(cuentaGuardada.getEmail()).isEqualTo("usuario1@example.com");
    }

    @Test
    public void testLeerCuenta() {
        Cuenta cuenta = new Cuenta("usuario2", "contrasena2", "usuario2@example.com");
        cuentaRepository.save(cuenta);

        Optional<Cuenta> cuentaEncontrada = cuentaRepository.findById(cuenta.getIdCuenta());

        assertThat(cuentaEncontrada).isPresent();
        assertThat(cuentaEncontrada.get().getIdCuenta()).isEqualTo(cuenta.getIdCuenta());
        assertThat(cuentaEncontrada.get().getNombreCuenta()).isEqualTo("usuario2");
        assertThat(cuentaEncontrada.get().getContrasena()).isEqualTo("contrasena2");
        assertThat(cuentaEncontrada.get().getEmail()).isEqualTo("usuario2@example.com");
    }

    @Test
    public void testActualizarCuenta() {
        Cuenta cuenta = new Cuenta("usuario3", "contrasena3", "usuario3@example.com");
        cuentaRepository.save(cuenta);

        cuenta.setContrasena("nuevaContrasena");
        Cuenta cuentaActualizada = cuentaRepository.save(cuenta);

        assertThat(cuentaActualizada.getIdCuenta()).isEqualTo(cuenta.getIdCuenta());
        assertThat(cuentaActualizada.getNombreCuenta()).isEqualTo("usuario3");
        assertThat(cuentaActualizada.getContrasena()).isEqualTo("nuevaContrasena");
        assertThat(cuentaActualizada.getEmail()).isEqualTo("usuario3@example.com");
    }

    // Prueba base de eliminacion, se espera que falle

    @Test
    public void testEliminarCuenta() {
        Cuenta cuenta = new Cuenta("usuario4", "contrasena4", "usuario4@example.com");
        cuentaRepository.save(cuenta);

        cuentaRepository.deleteById(cuenta.getIdCuenta());
        Optional<Cuenta> cuentaEliminada = cuentaRepository.findById(cuenta.getIdCuenta());

        assertThat(cuentaEliminada).isEmpty();
    }

    // Prueba para el correcto cambio de estado en la cuenta, debe funcionar en base a la logica de eliminacion.

    @Test
    public void testSQLDeleteLogic() {
        Cuenta cuenta = new Cuenta("usuario5", "contrasena5", "usuario5@example.com");
        cuentaRepository.save(cuenta);

        cuentaRepository.deleteById(cuenta.getIdCuenta());

        Optional<Cuenta> cuentaEliminada = cuentaRepository.findById(cuenta.getIdCuenta());

        assertThat(cuentaEliminada).isPresent();
        assertThat(cuentaEliminada.get().getEstado()).isEqualTo(Estado.INACTIVE);
    }
}
