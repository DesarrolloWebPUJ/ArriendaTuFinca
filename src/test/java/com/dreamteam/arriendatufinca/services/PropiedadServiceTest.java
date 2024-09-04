package com.dreamteam.arriendatufinca.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import com.dreamteam.arriendatufinca.dtos.CuentaDTO;
import com.dreamteam.arriendatufinca.dtos.propiedad.PropiedadDTO;
import com.dreamteam.arriendatufinca.dtos.propiedad.SimplePropiedadDTO;
import com.dreamteam.arriendatufinca.entities.Arrendador;
import com.dreamteam.arriendatufinca.entities.Propiedad;
import com.dreamteam.arriendatufinca.enums.Estado;
import com.dreamteam.arriendatufinca.exception.ManejadorErrores;
import com.dreamteam.arriendatufinca.repository.ArrendadorRepository;
import com.dreamteam.arriendatufinca.repository.PropiedadRepository;

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

public class PropiedadServiceTest {

    @Mock
    private PropiedadRepository propiedadRepository;

    @Mock
    private ArrendadorRepository arrendadorRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private UtilityService utilityService;

    @InjectMocks
    private PropiedadService propiedadService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSaveNewPropiedad() {
        SimplePropiedadDTO propiedadDTO = new SimplePropiedadDTO();
        propiedadDTO.setMunicipio("Cartagena");
        propiedadDTO.setDepartamento("Bolívar");
        propiedadDTO.setArrendador(new CuentaDTO());
        propiedadDTO.getArrendador().setIdCuenta(1);

        Arrendador arrendador = new Arrendador();
        arrendador.setIdCuenta(1);

        when(arrendadorRepository.findById(1)).thenReturn(Optional.of(arrendador));

        Propiedad propiedad = new Propiedad();
        propiedad.setIdPropiedad(1);
        when(modelMapper.map(propiedadDTO, Propiedad.class)).thenReturn(propiedad);
        when(propiedadRepository.save(any(Propiedad.class))).thenReturn(propiedad);

        SimplePropiedadDTO savedPropiedadDTO = new SimplePropiedadDTO();
        savedPropiedadDTO.setIdPropiedad(1);
        when(modelMapper.map(propiedad, SimplePropiedadDTO.class)).thenReturn(savedPropiedadDTO);

        ResponseEntity<SimplePropiedadDTO> response = propiedadService.saveNewPropiedad(propiedadDTO);

        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getIdPropiedad()).isEqualTo(1);
        verify(propiedadRepository).save(any(Propiedad.class));
    }

    @Test
    public void testSaveNewPropiedad_ArrendadorNotFound() {
        SimplePropiedadDTO propiedadDTO = new SimplePropiedadDTO();
        propiedadDTO.setArrendador(new CuentaDTO());
        propiedadDTO.getArrendador().setIdCuenta(1);

        when(arrendadorRepository.findById(1)).thenReturn(Optional.empty());

        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, ManejadorErrores.ERROR_ARRENDADOR_NO_EXISTE))
            .when(utilityService).verificarAusencia(any(), eq(ManejadorErrores.ERROR_ARRENDADOR_NO_EXISTE));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            propiedadService.saveNewPropiedad(propiedadDTO);
        });

        assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(exception.getReason()).isEqualTo(ManejadorErrores.ERROR_ARRENDADOR_NO_EXISTE);
    }

    @Test
    public void testUpdatePropiedad() {
        SimplePropiedadDTO propiedadDTO = new SimplePropiedadDTO();
        propiedadDTO.setIdPropiedad(1);
        propiedadDTO.setArrendador(new CuentaDTO());
        propiedadDTO.getArrendador().setIdCuenta(1);

        Propiedad propiedad = new Propiedad();
        propiedad.setIdPropiedad(1);
        Arrendador arrendador = new Arrendador();
        arrendador.setIdCuenta(1);
        propiedad.setArrendador(arrendador);

        when(propiedadRepository.findById(1)).thenReturn(Optional.of(propiedad));
        when(arrendadorRepository.findById(1)).thenReturn(Optional.of(arrendador));
        when(propiedadRepository.save(any(Propiedad.class))).thenReturn(propiedad);
        when(modelMapper.map(any(Propiedad.class), eq(SimplePropiedadDTO.class))).thenReturn(propiedadDTO);

        ResponseEntity<SimplePropiedadDTO> response = propiedadService.updatePropiedad(propiedadDTO);

        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getIdPropiedad()).isEqualTo(1);
        verify(propiedadRepository).save(any(Propiedad.class));
    }

    @Test
    public void testUpdatePropiedad_NotFound() {
        SimplePropiedadDTO propiedadDTO = new SimplePropiedadDTO();
        propiedadDTO.setIdPropiedad(1);
        propiedadDTO.setArrendador(new CuentaDTO());
        propiedadDTO.getArrendador().setIdCuenta(1);

        when(propiedadRepository.findById(1)).thenReturn(Optional.empty());

        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, ManejadorErrores.ERROR_PROPIEDAD_NO_EXISTE))
            .when(utilityService).verificarAusencia(any(), eq(ManejadorErrores.ERROR_PROPIEDAD_NO_EXISTE));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            propiedadService.updatePropiedad(propiedadDTO);
        });

        assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(exception.getReason()).isEqualTo(ManejadorErrores.ERROR_PROPIEDAD_NO_EXISTE);
    }

    @Test
    public void testGetPropiedades() {
        Propiedad propiedad1 = new Propiedad();
        propiedad1.setIdPropiedad(1);
        Propiedad propiedad2 = new Propiedad();
        propiedad2.setIdPropiedad(2);

        when(propiedadRepository.findAll()).thenReturn(Arrays.asList(propiedad1, propiedad2));

        PropiedadDTO propiedadDTO1 = new PropiedadDTO();
        propiedadDTO1.setIdPropiedad(1);
        PropiedadDTO propiedadDTO2 = new PropiedadDTO();
        propiedadDTO2.setIdPropiedad(2);

        when(modelMapper.map(propiedad1, PropiedadDTO.class)).thenReturn(propiedadDTO1);
        when(modelMapper.map(propiedad2, PropiedadDTO.class)).thenReturn(propiedadDTO2);

        List<PropiedadDTO> propiedades = propiedadService.getPropiedades();

        assertThat(propiedades).hasSize(2);
        assertThat(propiedades.get(0).getIdPropiedad()).isEqualTo(1);
        assertThat(propiedades.get(1).getIdPropiedad()).isEqualTo(2);
    }

    @Test
    public void testGetPropiedadById() {
        Propiedad propiedad = new Propiedad();
        propiedad.setIdPropiedad(1);

        when(propiedadRepository.findById(1)).thenReturn(Optional.of(propiedad));

        PropiedadDTO propiedadDTO = new PropiedadDTO();
        propiedadDTO.setIdPropiedad(1);

        when(modelMapper.map(propiedad, PropiedadDTO.class)).thenReturn(propiedadDTO);

        ResponseEntity<PropiedadDTO> response = propiedadService.getPropiedad(1);

        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getIdPropiedad()).isEqualTo(1);
    }

    @Test
    public void testGetPropiedadById_NotFound() {
        when(propiedadRepository.findById(1)).thenReturn(Optional.empty());

        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, ManejadorErrores.ERROR_PROPIEDAD_NO_EXISTE))
            .when(utilityService).verificarAusencia(any(), eq(ManejadorErrores.ERROR_PROPIEDAD_NO_EXISTE));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            propiedadService.getPropiedad(1);
        });

        assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(exception.getReason()).isEqualTo(ManejadorErrores.ERROR_PROPIEDAD_NO_EXISTE);
    }

    @Test
    public void testDesactivarPropiedad() {
        Propiedad propiedad = new Propiedad();
        propiedad.setIdPropiedad(1);
        propiedad.setEstado(Estado.ACTIVE);

        when(propiedadRepository.findById(1)).thenReturn(Optional.of(propiedad));

        propiedadService.desactivarPropiedad(1);

        verify(propiedadRepository).save(propiedad);
        assertThat(propiedad.getEstado()).isEqualTo(Estado.INACTIVE);
    }

    @Test
    public void testDesactivarPropiedad_NotFound() {
        when(propiedadRepository.findById(1)).thenReturn(Optional.empty());

        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, ManejadorErrores.ERROR_PROPIEDAD_NO_EXISTE))
            .when(utilityService).verificarAusencia(any(), eq(ManejadorErrores.ERROR_PROPIEDAD_NO_EXISTE));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            propiedadService.desactivarPropiedad(1);
        });

        assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(exception.getReason()).isEqualTo(ManejadorErrores.ERROR_PROPIEDAD_NO_EXISTE);
    }
}