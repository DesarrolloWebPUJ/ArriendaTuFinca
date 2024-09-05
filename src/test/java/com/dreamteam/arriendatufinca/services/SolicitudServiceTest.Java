package com.dreamteam.arriendatufinca.services;

import com.dreamteam.arriendatufinca.dtos.EstadoSolicitudDTO;
import com.dreamteam.arriendatufinca.dtos.propiedad.SimplePropiedadDTO;
import com.dreamteam.arriendatufinca.dtos.solicitud.SimpleSolicitudDTO;
import com.dreamteam.arriendatufinca.dtos.solicitud.SolicitudDTO;
import com.dreamteam.arriendatufinca.entities.EstadoSolicitud;
import com.dreamteam.arriendatufinca.entities.Propiedad;
import com.dreamteam.arriendatufinca.entities.Solicitud;
import com.dreamteam.arriendatufinca.enums.SolicitudStatus;
import com.dreamteam.arriendatufinca.exception.ManejadorErrores;
import com.dreamteam.arriendatufinca.repository.EstadoSolicitudRepository;
import com.dreamteam.arriendatufinca.repository.PropiedadRepository;
import com.dreamteam.arriendatufinca.repository.SolicitudRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllSolicitudes_Success() {
        List<Solicitud> solicitudes = List.of(new Solicitud(), new Solicitud());
        when(solicitudRepository.findAll()).thenReturn(solicitudes);
        when(modelMapper.map(any(Solicitud.class), eq(SolicitudDTO.class))).thenReturn(new SolicitudDTO());
    
        List<SolicitudDTO> result = solicitudService.getAllSolicitudes();
    
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(solicitudRepository, times(1)).findAll();
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
    void testUpdateSolicitud_Success() {
        SimpleSolicitudDTO solicitudDTO = new SimpleSolicitudDTO();
        solicitudDTO.setIdSolicitud(1);
        SolicitudStatus solicitudStatus = SolicitudStatus.PENDIENTE;
        solicitudDTO.setEstadoSolicitud(new EstadoSolicitudDTO(solicitudStatus.getId(), solicitudStatus.getNombre()));
        solicitudDTO.setArrendadorCalificado(true);
        solicitudDTO.setArrendatarioCalificado(true);
        solicitudDTO.setPropiedadCalificado(true);
    
        Solicitud solicitud = new Solicitud();
        solicitud.setIdSolicitud(1);
        
        solicitud.setEstadoSolicitud(new EstadoSolicitud(solicitudStatus.getId(), solicitudStatus.getNombre()));
        when(solicitudRepository.findById(1)).thenReturn(Optional.of(solicitud));
        when(solicitudRepository.save(any(Solicitud.class))).thenReturn(solicitud);
        when(modelMapper.map(any(SimpleSolicitudDTO.class), eq(Solicitud.class))).thenReturn(solicitud);
        when(modelMapper.map(any(Solicitud.class), eq(SimpleSolicitudDTO.class))).thenReturn(solicitudDTO);
    
        ResponseEntity<SimpleSolicitudDTO> result = solicitudService.updateSolicitud(solicitudDTO);
    
        assertNotNull(result);
        assertEquals(1, result.getBody().getIdSolicitud());
        assertTrue(result.getBody().isArrendadorCalificado());
        assertTrue(result.getBody().isArrendatarioCalificado());
        assertTrue(result.getBody().isPropiedadCalificado());
        verify(solicitudRepository, times(2)).save(any(Solicitud.class));
    }

    @Test
    void testGetSolicitudById_NotFound() {
        when(solicitudRepository.findById(1)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            solicitudService.getSolicitudById(1);
        });

        assertEquals(ManejadorErrores.ERROR_SOLICITUD_NO_EXISTE, exception.getReason());

        verify(solicitudRepository, times(1)).findById(1);
    }

    @Test
    void testSaveSolicitud_Success() {
        SimpleSolicitudDTO solicitudDTO = new SimpleSolicitudDTO();
        solicitudDTO.setFechaInicio(LocalDateTime.now().plusDays(1));
        solicitudDTO.setFechaFinal(LocalDateTime.now().plusDays(3));  // Fecha de mañana
        solicitudDTO.setCantidadPersonas(2);

        SimplePropiedadDTO propiedadDTO = new SimplePropiedadDTO();
        propiedadDTO.setIdPropiedad(1);
        Propiedad propiedad = new Propiedad();
        propiedad.setIdPropiedad(1);
        propiedad.setCantidadHabitaciones(3);

        solicitudDTO.setPropiedad(propiedadDTO);

        when(propiedadRepository.findById(1)).thenReturn(Optional.of(propiedad));
        when(solicitudRepository.save(any(Solicitud.class))).thenReturn(new Solicitud());
        SolicitudStatus solicitudStatus = SolicitudStatus.PENDIENTE;
        when(estadoSolicitudRepository.findById(1)).thenReturn(Optional.of(new EstadoSolicitud(solicitudStatus.getId(), solicitudStatus.getNombre())));
        when(modelMapper.map(any(SimpleSolicitudDTO.class), eq(Solicitud.class))).thenReturn(new Solicitud());
        when(modelMapper.map(any(Solicitud.class), eq(SimpleSolicitudDTO.class))).thenReturn(solicitudDTO);

        ResponseEntity<SimpleSolicitudDTO> result = solicitudService.saveSolicitud(solicitudDTO);

        assertNotNull(result);
        verify(solicitudRepository, times(1)).save(any(Solicitud.class));
        verify(propiedadRepository, times(1)).findById(1);
    }

    @Test
    void testSaveSolicitud_InvalidFechaInicio() {
        Propiedad propiedad = new Propiedad();
        propiedad.setIdPropiedad(1);
        SimplePropiedadDTO propiedadDTO = new SimplePropiedadDTO();
        propiedadDTO.setIdPropiedad(1);
        SimpleSolicitudDTO solicitudDTO = new SimpleSolicitudDTO();
        solicitudDTO.setFechaInicio(LocalDateTime.now().minusDays(1));  // Fecha de ayer
        solicitudDTO.setPropiedad(propiedadDTO);

        when(propiedadRepository.findById(1)).thenReturn(Optional.of(propiedad));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            solicitudService.saveSolicitud(solicitudDTO);
        });

        assertEquals(ManejadorErrores.ERROR_FECHA_INICIAL_SOLICITUD_INVALIDA, exception.getReason());
        verify(solicitudRepository, never()).save(any(Solicitud.class));
    }

    @Test
    void testSaveSolicitud_InvalidFechaFinal() {
        Propiedad propiedad = new Propiedad();
        propiedad.setIdPropiedad(1);
        SimplePropiedadDTO propiedadDTO = new SimplePropiedadDTO();
        propiedadDTO.setIdPropiedad(1);
        SimpleSolicitudDTO solicitudDTO = new SimpleSolicitudDTO();
        solicitudDTO.setFechaInicio(LocalDateTime.now().plusDays(1));
        solicitudDTO.setFechaFinal(LocalDateTime.now());
        solicitudDTO.setPropiedad(propiedadDTO);

        when(propiedadRepository.findById(1)).thenReturn(Optional.of(propiedad));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            solicitudService.saveSolicitud(solicitudDTO);
        });

        assertEquals(ManejadorErrores.ERROR_FECHA_FINAL_SOLICITUD_INVALIDA, exception.getReason());
        verify(solicitudRepository, never()).save(any(Solicitud.class));
    }

    @Test
    void testSaveSolicitud_InvalidCantidadPersonas() {
        Propiedad propiedad = new Propiedad();
        propiedad.setIdPropiedad(1);
        SimplePropiedadDTO propiedadDTO = new SimplePropiedadDTO();
        propiedadDTO.setIdPropiedad(1);
        SimpleSolicitudDTO solicitudDTO = new SimpleSolicitudDTO();
        solicitudDTO.setFechaInicio(LocalDateTime.now().plusDays(1));
        solicitudDTO.setFechaFinal(LocalDateTime.now().plusDays(3));
        solicitudDTO.setCantidadPersonas(0);
        solicitudDTO.setPropiedad(propiedadDTO);

        when(propiedadRepository.findById(1)).thenReturn(Optional.of(propiedad));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            solicitudService.saveSolicitud(solicitudDTO);
        });

        assertEquals(ManejadorErrores.ERROR_CANTIDAD_PERSONAS_SOLICITUD_INVALIDA, exception.getReason());
        verify(solicitudRepository, never()).save(any(Solicitud.class));
    }

    @Test
    void testUpdateSolicitud_InvalidEstado() {
        SimpleSolicitudDTO solicitudDTO = new SimpleSolicitudDTO();
        solicitudDTO.setIdSolicitud(1);
        solicitudDTO.setEstadoSolicitud(new EstadoSolicitudDTO(99, "INVALIDO"));

        Solicitud solicitud = new Solicitud();
        solicitud.setIdSolicitud(1);
        SolicitudStatus solicitudStatus = SolicitudStatus.PENDIENTE;
        solicitud.setEstadoSolicitud(new EstadoSolicitud(solicitudStatus.getId(), solicitudStatus.getNombre()));
    
        when(solicitudRepository.findById(1)).thenReturn(Optional.of(solicitud));
    
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            solicitudService.updateSolicitud(solicitudDTO);
        });
    
        assertEquals(ManejadorErrores.ERROR_CAMBIO_ESTADO_INVALIDO, exception.getReason());
        verify(solicitudRepository, never()).save(any(Solicitud.class));
    }

    @Test
    void testActualizarEstadoSolicitud_Success() {
        SolicitudStatus solicitudStatus = SolicitudStatus.POR_PAGAR;
        EstadoSolicitudDTO estadoSolicitudDTO = new EstadoSolicitudDTO(solicitudStatus.getId(), solicitudStatus.getNombre());
        Solicitud solicitud = new Solicitud();
        solicitud.setIdSolicitud(1);
        solicitudStatus = SolicitudStatus.PENDIENTE;
        solicitud.setEstadoSolicitud(new EstadoSolicitud(solicitudStatus.getId(), solicitudStatus.getNombre()));
    
        when(solicitudRepository.findById(1)).thenReturn(Optional.of(solicitud));
        when(estadoSolicitudRepository.findById(2)).thenReturn(Optional.of(new EstadoSolicitud(2, "POR_PAGAR")));
        when(solicitudRepository.save(any(Solicitud.class))).thenReturn(solicitud);
        when(modelMapper.map(any(Solicitud.class), eq(SimpleSolicitudDTO.class))).thenReturn(new SimpleSolicitudDTO());
    
        ResponseEntity<SimpleSolicitudDTO> result = solicitudService.actualizarEstadoSolicitud(estadoSolicitudDTO, 1);
    
        assertNotNull(result);
        verify(solicitudRepository, times(1)).save(any(Solicitud.class));
    }

    @Test
    void testUpdateSolicitud_TransicionPendienteARechazada() {
        SimpleSolicitudDTO solicitudDTO = new SimpleSolicitudDTO();
        solicitudDTO.setIdSolicitud(1);
        solicitudDTO.setEstadoSolicitud(new EstadoSolicitudDTO(SolicitudStatus.RECHAZADA.getId(), SolicitudStatus.RECHAZADA.getNombre()));

        Solicitud solicitud = new Solicitud();
        solicitud.setIdSolicitud(1);
        solicitud.setEstadoSolicitud(new EstadoSolicitud(SolicitudStatus.PENDIENTE.getId(), SolicitudStatus.PENDIENTE.getNombre()));

        when(solicitudRepository.findById(1)).thenReturn(Optional.of(solicitud));
        when(estadoSolicitudRepository.findById(SolicitudStatus.RECHAZADA.getId())).thenReturn(Optional.of(new EstadoSolicitud(SolicitudStatus.RECHAZADA.getId(), SolicitudStatus.RECHAZADA.getNombre())));
        when(solicitudRepository.save(any(Solicitud.class))).thenReturn(solicitud);
        when(modelMapper.map(any(Solicitud.class), eq(SimpleSolicitudDTO.class))).thenReturn(solicitudDTO);

        ResponseEntity<SimpleSolicitudDTO> result = solicitudService.updateSolicitud(solicitudDTO);

        assertNotNull(result);
        assertEquals(SolicitudStatus.RECHAZADA.getNombre(), result.getBody().getEstadoSolicitud().getNombreEstadoSolicitud());
        verify(solicitudRepository, times(1)).save(any(Solicitud.class));
    }

    @Test
    void testUpdateSolicitud_TransicionPorPagarAPorCalificar() {
        SimpleSolicitudDTO solicitudDTO = new SimpleSolicitudDTO();
        solicitudDTO.setIdSolicitud(1);
        solicitudDTO.setEstadoSolicitud(new EstadoSolicitudDTO(SolicitudStatus.POR_CALIFICAR.getId(), SolicitudStatus.POR_CALIFICAR.getNombre()));

        Solicitud solicitud = new Solicitud();
        solicitud.setIdSolicitud(1);
        solicitud.setEstadoSolicitud(new EstadoSolicitud(SolicitudStatus.POR_PAGAR.getId(), SolicitudStatus.POR_PAGAR.getNombre()));

        when(solicitudRepository.findById(1)).thenReturn(Optional.of(solicitud));
        when(estadoSolicitudRepository.findById(SolicitudStatus.POR_CALIFICAR.getId())).thenReturn(Optional.of(new EstadoSolicitud(SolicitudStatus.POR_CALIFICAR.getId(), SolicitudStatus.POR_CALIFICAR.getNombre())));
        when(solicitudRepository.save(any(Solicitud.class))).thenReturn(solicitud);
        when(modelMapper.map(any(Solicitud.class), eq(SimpleSolicitudDTO.class))).thenReturn(solicitudDTO);

        ResponseEntity<SimpleSolicitudDTO> result = solicitudService.updateSolicitud(solicitudDTO);

        assertNotNull(result);
        assertEquals(SolicitudStatus.POR_CALIFICAR.getNombre(), result.getBody().getEstadoSolicitud().getNombreEstadoSolicitud());
        verify(solicitudRepository, times(1)).save(any(Solicitud.class));
    }

    @Test
    void testUpdateSolicitud_TransicionPorCalificarACerrada() {
        SimpleSolicitudDTO solicitudDTO = new SimpleSolicitudDTO();
        solicitudDTO.setIdSolicitud(1);
        solicitudDTO.setEstadoSolicitud(new EstadoSolicitudDTO(SolicitudStatus.CERRADA.getId(), SolicitudStatus.CERRADA.getNombre()));

        Solicitud solicitud = new Solicitud();
        solicitud.setIdSolicitud(1);
        solicitud.setEstadoSolicitud(new EstadoSolicitud(SolicitudStatus.POR_CALIFICAR.getId(), SolicitudStatus.POR_CALIFICAR.getNombre()));

        when(solicitudRepository.findById(1)).thenReturn(Optional.of(solicitud));
        when(estadoSolicitudRepository.findById(SolicitudStatus.CERRADA.getId())).thenReturn(Optional.of(new EstadoSolicitud(SolicitudStatus.CERRADA.getId(), SolicitudStatus.CERRADA.getNombre())));
        when(solicitudRepository.save(any(Solicitud.class))).thenReturn(solicitud);
        when(modelMapper.map(any(Solicitud.class), eq(SimpleSolicitudDTO.class))).thenReturn(solicitudDTO);

        ResponseEntity<SimpleSolicitudDTO> result = solicitudService.updateSolicitud(solicitudDTO);

        assertNotNull(result);
        assertEquals(SolicitudStatus.CERRADA.getNombre(), result.getBody().getEstadoSolicitud().getNombreEstadoSolicitud());
        verify(solicitudRepository, times(1)).save(any(Solicitud.class));
    }

    @Test
    void testActualizarEstadoSolicitud_InvalidTransition() {
        EstadoSolicitudDTO estadoSolicitudDTO = new EstadoSolicitudDTO(99, "INVALIDO");
        Solicitud solicitud = new Solicitud();
        solicitud.setIdSolicitud(1);
        solicitud.setEstadoSolicitud(new EstadoSolicitud(1, "PENDIENTE"));
    
        when(solicitudRepository.findById(1)).thenReturn(Optional.of(solicitud));
    
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            solicitudService.actualizarEstadoSolicitud(estadoSolicitudDTO, 1);
        });
    
        assertEquals(ManejadorErrores.ERROR_CAMBIO_ESTADO_INVALIDO, exception.getReason());
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

}
