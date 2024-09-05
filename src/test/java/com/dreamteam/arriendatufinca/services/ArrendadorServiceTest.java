package com.dreamteam.arriendatufinca.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import com.dreamteam.arriendatufinca.dtos.ArrendadorDTO;
import com.dreamteam.arriendatufinca.dtos.CuentaDTO;
import com.dreamteam.arriendatufinca.entities.Arrendador;
import com.dreamteam.arriendatufinca.enums.Estado;
import com.dreamteam.arriendatufinca.exception.ManejadorErrores;
import com.dreamteam.arriendatufinca.repository.ArrendadorRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

class ArrendadorServiceTest {

    @Mock
    private ArrendadorRepository arrendadorRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private UtilityService utilityService;

    @InjectMocks
    private ArrendadorService arrendadorService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetArrendadores() {
        Arrendador arrendador1 = new Arrendador("arrendador1", "contrasena1", "arrendador1@example.com");
        Arrendador arrendador2 = new Arrendador("arrendador2", "contrasena2", "arrendador2@example.com");

        when(arrendadorRepository.findAll()).thenReturn(Arrays.asList(arrendador1, arrendador2));

        CuentaDTO cuentaDTO1 = new CuentaDTO();
        cuentaDTO1.setNombreCuenta("arrendador1");
        CuentaDTO cuentaDTO2 = new CuentaDTO();
        cuentaDTO2.setNombreCuenta("arrendador2");

        when(modelMapper.map(arrendador1, CuentaDTO.class)).thenReturn(cuentaDTO1);
        when(modelMapper.map(arrendador2, CuentaDTO.class)).thenReturn(cuentaDTO2);

        List<CuentaDTO> arrendadores = arrendadorService.getArrendadores();

        assertThat(arrendadores).hasSize(2);
        assertThat(arrendadores.get(0).getNombreCuenta()).isEqualTo("arrendador1");
        assertThat(arrendadores.get(1).getNombreCuenta()).isEqualTo("arrendador2");
    }

    @Test
    void testGetArrendador() {
        Arrendador arrendador = new Arrendador("arrendador1", "contrasena1", "arrendador1@example.com");
        arrendador.setIdCuenta(1);

        when(arrendadorRepository.findById(1)).thenReturn(Optional.of(arrendador));

        ArrendadorDTO arrendadorDTO = new ArrendadorDTO();
        arrendadorDTO.setIdCuenta(1);
        arrendadorDTO.setNombreCuenta("arrendador1");

        when(modelMapper.map(arrendador, ArrendadorDTO.class)).thenReturn(arrendadorDTO);

        ResponseEntity<ArrendadorDTO> response = arrendadorService.getArrendador(1);

        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getIdCuenta()).isEqualTo(1);
        assertThat(response.getBody().getNombreCuenta()).isEqualTo("arrendador1");
    }

    @Test
    void testGetArrendador_NotFound() {
        when(arrendadorRepository.findById(1)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            arrendadorService.getArrendador(1);
        });

        assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(exception.getReason()).isEqualTo(ManejadorErrores.ERROR_ARRENDADOR_NO_EXISTE);
    }

    @Test
    void testSaveNewArrendador() {
        ArrendadorDTO arrendadorDTO = new ArrendadorDTO();
        arrendadorDTO.setEmail("arrendador1@example.com");

        when(arrendadorRepository.findByEmail("arrendador1@example.com")).thenReturn(Optional.empty());

        Arrendador arrendador = new Arrendador();
        arrendador.setEmail("arrendador1@example.com");

        when(modelMapper.map(arrendadorDTO, Arrendador.class)).thenReturn(arrendador);
        when(arrendadorRepository.save(any(Arrendador.class))).thenReturn(arrendador);

        ResponseEntity<ArrendadorDTO> response = arrendadorService.saveNewArrendador(arrendadorDTO);

        assertThat(response.getBody()).isNotNull();
        verify(arrendadorRepository).save(arrendador);
        assertThat(arrendador.getEstado()).isEqualTo(Estado.ACTIVE);
    }

    @Test
    void testSaveNewArrendador_EmailAlreadyExists() {
        ArrendadorDTO arrendadorDTO = new ArrendadorDTO();
        arrendadorDTO.setEmail("arrendador1@example.com");

        Arrendador existingArrendador = new Arrendador();
        existingArrendador.setEmail("arrendador1@example.com");

        when(arrendadorRepository.findByEmail("arrendador1@example.com")).thenReturn(Optional.of(existingArrendador));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            arrendadorService.saveNewArrendador(arrendadorDTO);
        });

        assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(exception.getReason()).isEqualTo(ManejadorErrores.ERROR_CORREO_ARRENDADOR_YA_EXISTE);
    }
}
