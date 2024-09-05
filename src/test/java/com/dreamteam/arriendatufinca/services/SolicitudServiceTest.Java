package com.dreamteam.arriendatufinca.services;

import com.dreamteam.arriendatufinca.dtos.solicitud.SimpleSolicitudDTO;
import com.dreamteam.arriendatufinca.entities.Propiedad;
import com.dreamteam.arriendatufinca.entities.Solicitud;
import com.dreamteam.arriendatufinca.exception.ManejadorErrores;
import com.dreamteam.arriendatufinca.repository.EstadoSolicitudRepository;
import com.dreamteam.arriendatufinca.repository.PropiedadRepository;
import com.dreamteam.arriendatufinca.repository.SolicitudRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

class SolicitudServiceTest {

    @Mock
    private SolicitudRepository solicitudRepository;

    @Mock
    private PropiedadRepository propiedadRepository;

    @Mock
    private EstadoSolicitudRepository estadoSolicitudRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private SolicitudService solicitudService;

    @BeforeEach
    void setUp() {
        initMocks(this);
    }

    @Test
    void testGetSolicitudById_Success() {
        Solicitud solicitud = new Solicitud();
        solicitud.setIdSolicitud(1);

        SimpleSolicitudDTO solicitudDTO = new SimpleSolicitudDTO();
        solicitudDTO.setIdSolicitud(1);

        when(solicitudRepository.findById(1)).thenReturn(Optional.of(solicitud));
        when(modelMapper.map(any(Solicitud.class), eq(SimpleSolicitudDTO.class))).thenReturn(solicitudDTO);

        ResponseEntity<SimpleSolicitudDTO> result = solicitudService.getSolicitudById(1);
        
        assertNotNull(result);
        assertEquals(1, result.getBody().getIdSolicitud());
        verify(solicitudRepository, times(1)).findById(1);
    }

    @Test
    void testGetSolicitudById_NotFound() {
        when(solicitudRepository.findById(1)).thenReturn(Optional.empty());

        ManejadorErrores manejadorErrores = assertThrows(ManejadorErrores.class, () -> {
            solicitudService.getSolicitudById(1);
        });

        assertEquals(ManejadorErrores.ERROR_SOLICITUD_NO_EXISTE, manejadorErrores.getMessage());
        verify(solicitudRepository, times(1)).findById(1);
    }

    @Test
    void testSaveSolicitud_Success() {
        SimpleSolicitudDTO solicitudDTO = new SimpleSolicitudDTO();
        solicitudDTO.setFechaInicio(new Date());
        solicitudDTO.setFechaFinal(new Date(System.currentTimeMillis() + 86400000));  // Fecha de mañana
        solicitudDTO.setCantidadPersonas(2);

        Propiedad propiedad = new Propiedad();
        propiedad.setIdPropiedad(1);
        propiedad.setCantidadHabitaciones(3);

        when(propiedadRepository.findById(1)).thenReturn(Optional.of(propiedad));
        when(solicitudRepository.save(any(Solicitud.class))).thenReturn(new Solicitud());

        ResponseEntity<SimpleSolicitudDTO> result = solicitudService.saveSolicitud(solicitudDTO);

        assertNotNull(result);
        verify(solicitudRepository, times(1)).save(any(Solicitud.class));
        verify(propiedadRepository, times(1)).findById(1);
    }

    @Test
    void testSaveSolicitud_InvalidFechaInicio() {
        SimpleSolicitudDTO solicitudDTO = new SimpleSolicitudDTO();
        solicitudDTO.setFechaInicio(new Date(System.currentTimeMillis() - 86400000));  // Fecha de ayer

        ManejadorErrores manejadorErrores = assertThrows(ManejadorErrores.class, () -> {
            solicitudService.saveSolicitud(solicitudDTO);
        });

        assertEquals(ManejadorErrores.ERROR_FECHA_INICIAL_SOLICITUD_INVALIDA, manejadorErrores.getMessage());
        verify(solicitudRepository, never()).save(any(Solicitud.class));
    }

    @Test
    void testDeleteSolicitud_Success() {
        Solicitud solicitud = new Solicitud();
        solicitud.setIdSolicitud(1);

        when(solicitudRepository.findById(1)).thenReturn(Optional.of(solicitud));

        solicitudService.deleteSolicitud(1);

        verify(solicitudRepository, times(1)).delete(solicitud);
    }

    @Test
    void testDeleteSolicitud_NotFound() {
        when(solicitudRepository.findById(1)).thenReturn(Optional.empty());

        ManejadorErrores manejadorErrores = assertThrows(ManejadorErrores.class, () -> {
            solicitudService.deleteSolicitud(1);
        });

        assertEquals(ManejadorErrores.ERROR_SOLICITUD_NO_EXISTE, manejadorErrores.getMessage());
        verify(solicitudRepository, never()).delete(any(Solicitud.class));
    }
}
