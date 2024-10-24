package com.dreamteam.arriendatufinca.services;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import com.dreamteam.arriendatufinca.dtos.ArrendatarioDTO;
import com.dreamteam.arriendatufinca.dtos.CuentaDTO;
import com.dreamteam.arriendatufinca.dtos.validation.SignUpRequest;
import com.dreamteam.arriendatufinca.entities.Arrendatario;
import com.dreamteam.arriendatufinca.enums.Estado;
import com.dreamteam.arriendatufinca.exception.ManejadorErrores;
import com.dreamteam.arriendatufinca.repository.ArrendatarioRepository;

class ArrendatarioServiceTest {

    @Mock
    private ArrendatarioRepository arrendatarioRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private UtilityService utilityService;

    @InjectMocks
    private ArrendatarioService arrendatarioService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetArrendatarios() {
        Arrendatario arrendatario1 = new Arrendatario("arrendatario1", "contrasena1", "arrendatario1@example.com", "apellido1", "telefono1");
        Arrendatario arrendatario2 = new Arrendatario("arrendatario2", "contrasena2", "arrendatario2@example.com", "apellido1", "telefono1");

        when(arrendatarioRepository.findAll()).thenReturn(Arrays.asList(arrendatario1, arrendatario2));

        CuentaDTO cuentaDTO1 = new CuentaDTO();
        cuentaDTO1.setNombreCuenta("arrendatario1");
        CuentaDTO cuentaDTO2 = new CuentaDTO();
        cuentaDTO2.setNombreCuenta("arrendatario2");

        when(modelMapper.map(arrendatario1, CuentaDTO.class)).thenReturn(cuentaDTO1);
        when(modelMapper.map(arrendatario2, CuentaDTO.class)).thenReturn(cuentaDTO2);

        List<CuentaDTO> arrendatarios = arrendatarioService.getArrendatarios();

        assertThat(arrendatarios).hasSize(2);
        assertThat(arrendatarios.get(0).getNombreCuenta()).isEqualTo("arrendatario1");
        assertThat(arrendatarios.get(1).getNombreCuenta()).isEqualTo("arrendatario2");
    }

    @Test
    void testGetArrendatario() {
        Arrendatario arrendatario = new Arrendatario("arrendatario1", "contrasena1", "arrendatario1@example.com", "apellido1", "telefono1");
        arrendatario.setIdCuenta(1);

        when(arrendatarioRepository.findById(1)).thenReturn(Optional.of(arrendatario));

        ArrendatarioDTO arrendatarioDTO = new ArrendatarioDTO();
        arrendatarioDTO.setIdCuenta(1);
        arrendatarioDTO.setNombreCuenta("arrendatario1");

        when(modelMapper.map(arrendatario, ArrendatarioDTO.class)).thenReturn(arrendatarioDTO);

        ResponseEntity<ArrendatarioDTO> response = arrendatarioService.getArrendatario(1);

        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getIdCuenta()).isEqualTo(1);
        assertThat(response.getBody().getNombreCuenta()).isEqualTo("arrendatario1");
    }

    @Test
    void testGetArrendatario_NotFound() {
        when(arrendatarioRepository.findById(1)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            arrendatarioService.getArrendatario(1);
        });

        assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(exception.getReason()).isEqualTo(ManejadorErrores.ERROR_ARRENDATARIO_NO_EXISTE);
    }

    @Test
    void testSaveNewArrendatario() {
        ArrendatarioDTO arrendatarioDTO = new ArrendatarioDTO();
        arrendatarioDTO.setEmail("arrendatario1@example.com");

        when(arrendatarioRepository.findByEmail("arrendatario1@example.com")).thenReturn(Optional.empty());

        Arrendatario arrendatario = new Arrendatario();
        arrendatario.setEmail("arrendatario1@example.com");

        when(modelMapper.map(arrendatarioDTO, Arrendatario.class)).thenReturn(arrendatario);
        when(arrendatarioRepository.save(any(Arrendatario.class))).thenReturn(arrendatario);

        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setCuenta(arrendatarioDTO);
        ResponseEntity<CuentaDTO> response = arrendatarioService.saveNewArrendatario(signUpRequest);

        assertThat(response.getBody()).isNotNull();
        verify(arrendatarioRepository).save(arrendatario);
        assertThat(arrendatario.getEstado()).isEqualTo(Estado.ACTIVE);
    }

    @Test
    void testSaveNewArrendatario_EmailAlreadyExists() {
        ArrendatarioDTO arrendatarioDTO = new ArrendatarioDTO();
        arrendatarioDTO.setEmail("arrendatario1@example.com");

        Arrendatario existingArrendatario = new Arrendatario();
        existingArrendatario.setEmail("arrendatario1@example.com");

        when(arrendatarioRepository.findByEmail("arrendatario1@example.com")).thenReturn(Optional.of(existingArrendatario));

        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setCuenta(arrendatarioDTO);
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            arrendatarioService.saveNewArrendatario(signUpRequest);
        });

        assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(exception.getReason()).isEqualTo(ManejadorErrores.ERROR_CORREO_ARRENDATARIO_YA_EXISTE);
    }
}
