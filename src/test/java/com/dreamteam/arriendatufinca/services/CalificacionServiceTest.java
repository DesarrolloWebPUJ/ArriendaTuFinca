package com.dreamteam.arriendatufinca.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import com.dreamteam.arriendatufinca.dtos.CuentaDTO;
import com.dreamteam.arriendatufinca.dtos.calificacion.BaseCalificacionDTO;
import com.dreamteam.arriendatufinca.dtos.calificacion.CalificacionDTO;
import com.dreamteam.arriendatufinca.dtos.propiedad.BasePropiedadDTO;
import com.dreamteam.arriendatufinca.dtos.propiedad.SimplePropiedadDTO;
import com.dreamteam.arriendatufinca.dtos.solicitud.BaseSolicitudDTO;
import com.dreamteam.arriendatufinca.dtos.solicitud.SolicitudDTO;
import com.dreamteam.arriendatufinca.entities.Arrendador;
import com.dreamteam.arriendatufinca.entities.Arrendatario;
import com.dreamteam.arriendatufinca.entities.Calificacion;
import com.dreamteam.arriendatufinca.entities.Cuenta;
import com.dreamteam.arriendatufinca.entities.EstadoSolicitud;
import com.dreamteam.arriendatufinca.entities.Propiedad;
import com.dreamteam.arriendatufinca.entities.Solicitud;
import com.dreamteam.arriendatufinca.enums.SolicitudStatus;
import com.dreamteam.arriendatufinca.enums.TipoCalificacion;
import com.dreamteam.arriendatufinca.exception.ManejadorErrores;
import com.dreamteam.arriendatufinca.repository.CalificacionRepository;
import com.dreamteam.arriendatufinca.repository.CuentaRepository;
import com.dreamteam.arriendatufinca.repository.PropiedadRepository;
import com.dreamteam.arriendatufinca.repository.SolicitudRepository;

class CalificacionServiceTest {

    @InjectMocks
    private CalificacionService calificacionService;

    @Mock
    private CalificacionRepository calificacionRepository;

    @Mock
    private CuentaRepository cuentaRepository;

    @Mock
    private SolicitudRepository solicitudRepository;

    @Mock
    private PropiedadRepository propiedadRepository;

    @InjectMocks
    private ModelMapper realModelMapper;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private SolicitudService solicitudService;

    @Mock
    private PropiedadService propiedadService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetCalificaciones() {
        List<Calificacion> calificaciones = List.of(new Calificacion(), new Calificacion());
        when(calificacionRepository.findAll()).thenReturn(calificaciones);
        when(modelMapper.map(any(Calificacion.class), eq(CalificacionDTO.class))).thenReturn(new CalificacionDTO());

        List<CalificacionDTO> result = calificacionService.getCalificaciones();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(calificacionRepository, times(1)).findAll();
    }

    @Test
    void testGetCalificacionId_Success() {
        Calificacion calificacion = new Calificacion();
        when(calificacionRepository.findById(1)).thenReturn(Optional.of(calificacion));
        when(modelMapper.map(any(Calificacion.class), eq(CalificacionDTO.class))).thenReturn(new CalificacionDTO());

        ResponseEntity<CalificacionDTO> result = calificacionService.getCalificacionId(1);

        assertNotNull(result);
        assertEquals(200, result.getStatusCode().value());
        verify(calificacionRepository, times(1)).findById(1);
    }

    @Test
    void testGetCalificacionId_NotFound() {
        when(calificacionRepository.findById(1)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            calificacionService.getCalificacionId(1);
        });

        assertEquals(ManejadorErrores.ERROR_CALIFICACION_NO_EXISTE, exception.getReason());
        verify(calificacionRepository, times(1)).findById(1);
    }

    @Test
    void testGetCalificacionesCuenta_Success() {
        Cuenta cuenta = new Cuenta();
        when(cuentaRepository.findById(1)).thenReturn(Optional.of(cuenta));
        List<Calificacion> calificaciones = List.of(new Calificacion(), new Calificacion());
        when(calificacionRepository.findByIdCalificado(1)).thenReturn(calificaciones);
        when(modelMapper.map(any(Calificacion.class), eq(BaseCalificacionDTO.class))).thenReturn(new BaseCalificacionDTO());

        List<BaseCalificacionDTO> result = calificacionService.getCalificacionesCuenta(1);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(calificacionRepository, times(1)).findByIdCalificado(1);
    }

    @Test
    void testGetCalificacionesCuenta_NotFound() {
        when(cuentaRepository.findById(1)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            calificacionService.getCalificacionesCuenta(1);
        });

        assertEquals(ManejadorErrores.ERROR_CUENTA_NO_EXISTE, exception.getReason());
        verify(cuentaRepository, times(1)).findById(1);
    }

    @Test
    void testSaveNewCalificacion_Success() {
        CalificacionDTO calificacionDTO = new CalificacionDTO();
        Solicitud solicitud = new Solicitud();
        prepararCalificacionCorrecta(calificacionDTO, solicitud);
        solicitud.setArrendadorCalificado(true);
        solicitud.setPropiedadCalificado(true);
        SolicitudDTO solicitudDTO = realModelMapper.map(solicitud, SolicitudDTO.class);
        

        Calificacion calificacion = new Calificacion();
        calificacion.setSolicitud(new Solicitud());
        calificacion.getSolicitud().setEstadoSolicitud(new EstadoSolicitud(1, SolicitudStatus.POR_CALIFICAR.getNombre()));
        
        when(modelMapper.map(any(Solicitud.class), eq(SolicitudDTO.class))).thenReturn(solicitudDTO);
        when(cuentaRepository.findById(anyInt())).thenReturn(Optional.of(new Cuenta()));
        when(propiedadRepository.findById(anyInt())).thenReturn(Optional.of(new Propiedad()));
        when(solicitudRepository.findById(anyInt())).thenReturn(Optional.of(solicitud));
        when(modelMapper.map(any(CalificacionDTO.class), eq(Calificacion.class))).thenReturn(calificacion);
        when(calificacionRepository.save(any(Calificacion.class))).thenReturn(calificacion);
        when(modelMapper.map(any(Calificacion.class), eq(CalificacionDTO.class))).thenReturn(calificacionDTO);
        when(solicitudService.updateSolicitud(any(SolicitudDTO.class))).thenReturn(ResponseEntity.ok(solicitudDTO));

        ResponseEntity<CalificacionDTO> result = calificacionService.saveNewCalificacion(calificacionDTO);

        assertNotNull(result);
        assertEquals(200, result.getStatusCode().value());
        verify(calificacionRepository, times(1)).save(any(Calificacion.class));
    }

    @Test
    void testSaveNewCalificacion_CuentaRepetida() {
        CalificacionDTO calificacionDTO = new CalificacionDTO();
        Solicitud solicitud = new Solicitud();
        prepararCalificacionCorrecta(calificacionDTO, solicitud);
        calificacionDTO.getCalificado().setIdCuenta(1); // Same as calificador
        
        when(propiedadRepository.findById(anyInt())).thenReturn(Optional.of(new Propiedad()));
        when(cuentaRepository.findById(anyInt())).thenReturn(Optional.of(new Cuenta()));
        when(solicitudRepository.findById(anyInt())).thenReturn(Optional.of(solicitud));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            calificacionService.saveNewCalificacion(calificacionDTO);
        });

        assertEquals(ManejadorErrores.ERROR_CUENTA_REPETIDA, exception.getReason());
    }

    @Test
    void testSaveNewCalificacion_CalificadorNoPerteneceSolicitud() {
        CalificacionDTO calificacionDTO = new CalificacionDTO();
        Solicitud solicitud = new Solicitud();
        prepararCalificacionCorrecta(calificacionDTO, solicitud);
        calificacionDTO.getCalificador().setIdCuenta(3); // Not part of the solicitud
        
        when(propiedadRepository.findById(anyInt())).thenReturn(Optional.of(new Propiedad()));
        when(cuentaRepository.findById(anyInt())).thenReturn(Optional.of(new Cuenta()));
        when(solicitudRepository.findById(anyInt())).thenReturn(Optional.of(solicitud));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            calificacionService.saveNewCalificacion(calificacionDTO);
        });

        assertEquals(ManejadorErrores.ERROR_CALIFICADOR_NO_PERTENECE_SOLICITUD, exception.getReason());
    }

    @Test
    void testSaveNewCalificacion_CalificadoNoPerteneceSolicitud() {
        CalificacionDTO calificacionDTO = new CalificacionDTO();
        Solicitud solicitud = new Solicitud();
        prepararCalificacionCorrecta(calificacionDTO, solicitud);
        calificacionDTO.getCalificado().setIdCuenta(3); // Not part of the solicitud
        
        when(propiedadRepository.findById(anyInt())).thenReturn(Optional.of(new Propiedad()));
        when(cuentaRepository.findById(anyInt())).thenReturn(Optional.of(new Cuenta()));
        when(solicitudRepository.findById(anyInt())).thenReturn(Optional.of(solicitud));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            calificacionService.saveNewCalificacion(calificacionDTO);
        });

        assertEquals(ManejadorErrores.ERROR_CALIFICADO_NO_PERTENECE_SOLICITUD, exception.getReason());
    }

    @Test
    void testSaveNewCalificacion_PropiedadNoPerteneceSolicitud() {
        CalificacionDTO calificacionDTO = new CalificacionDTO();
        Solicitud solicitud = new Solicitud();
        prepararCalificacionCorrecta(calificacionDTO, solicitud);
        calificacionDTO.getPropiedad().setIdPropiedad(5);; // Not part of the solicitud
        
        when(propiedadRepository.findById(anyInt())).thenReturn(Optional.of(new Propiedad()));
        when(cuentaRepository.findById(anyInt())).thenReturn(Optional.of(new Cuenta()));
        when(solicitudRepository.findById(anyInt())).thenReturn(Optional.of(solicitud));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            calificacionService.saveNewCalificacion(calificacionDTO);
        });

        assertEquals(ManejadorErrores.ERROR_PROPIEDAD_NO_PERTENECE_SOLICITUD, exception.getReason());
    }

    @Test
    void testSaveNewCalificacion_InvalidPuntaje() {
        CalificacionDTO calificacionDTO = new CalificacionDTO();
        Solicitud solicitud = new Solicitud();
        prepararCalificacionCorrecta(calificacionDTO, solicitud);
        when(cuentaRepository.findById(anyInt())).thenReturn(Optional.of(new Cuenta()));
        when(propiedadRepository.findById(anyInt())).thenReturn(Optional.of(new Propiedad()));
        when(solicitudRepository.findById(anyInt())).thenReturn(Optional.of(solicitud));

        calificacionDTO.setPuntaje(6); // Invalid puntaje

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            calificacionService.saveNewCalificacion(calificacionDTO);
        });
        

        assertEquals(ManejadorErrores.ERROR_PUNTAJE_INVALIDO, exception.getReason());
        verify(calificacionRepository, never()).save(any(Calificacion.class));
    }

    @Test
    void testAsignarCalificacionSolicitud_SuccessArrendatarioToPropiedad() {
        CalificacionDTO calificacionDTO = new CalificacionDTO();
        Solicitud solicitud = new Solicitud();
        prepararCalificacionCorrecta(calificacionDTO, solicitud);
        calificacionDTO.setTipoCalificacion(TipoCalificacion.ARRENDATARIO_A_PROPIEDAD.getValue());

        when(solicitudRepository.findById(anyInt())).thenReturn(Optional.of(solicitud));
        when(propiedadService.updatePropiedad(any(SimplePropiedadDTO.class))).thenReturn(ResponseEntity.ok(new SimplePropiedadDTO()));

        SolicitudDTO solicitudDTO = new SolicitudDTO();
        solicitudDTO.setIdSolicitud(1);
        solicitudDTO.setPropiedad(new SimplePropiedadDTO());

        SolicitudDTO result = calificacionService.asignarCalificacionSolicitud(calificacionDTO, solicitudDTO);

        assertNotNull(result);
        assertTrue(result.isPropiedadCalificado());
        verify(solicitudRepository, times(1)).findById(anyInt());
    }

    @Test
    void testAsignarCalificacionSolicitud_InvalidTipoCalificacion() {
        CalificacionDTO calificacionDTO = new CalificacionDTO();
        Solicitud solicitud = new Solicitud();
        prepararCalificacionCorrecta(calificacionDTO, solicitud);
        calificacionDTO.setTipoCalificacion("INVALID_TYPE");


        when(solicitudRepository.findById(anyInt())).thenReturn(Optional.of(solicitud));

        SolicitudDTO solicitudDTO = new SolicitudDTO();
        solicitudDTO.setIdSolicitud(1);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            calificacionService.asignarCalificacionSolicitud(calificacionDTO, solicitudDTO);
        });

        assertEquals(ManejadorErrores.ERROR_TIPO_CALIFICACION_INVALIDO, exception.getReason());
    }

    private void prepararCalificacionCorrecta(CalificacionDTO calificacionDTO, Solicitud solicitud){
        calificacionDTO.setTipoCalificacion(TipoCalificacion.ARRENDADOR_A_ARRENDATARIO.getValue());
        calificacionDTO.setPuntaje(5);
        CuentaDTO arrendadorDTO = new CuentaDTO();
        arrendadorDTO.setIdCuenta(1);
        calificacionDTO.setCalificador(arrendadorDTO);
        CuentaDTO arrendatarioDTO = new CuentaDTO();
        calificacionDTO.setCalificado(arrendatarioDTO);
        arrendatarioDTO.setIdCuenta(2);
        
        BasePropiedadDTO propiedadDTO = new BasePropiedadDTO();
        propiedadDTO.setIdPropiedad(1);
        calificacionDTO.setPropiedad(propiedadDTO);
        BaseSolicitudDTO solicitudDTO = new BaseSolicitudDTO();
        solicitudDTO.setIdSolicitud(1);
        calificacionDTO.setSolicitud(solicitudDTO);

        solicitud.setIdSolicitud(1);
        solicitud.setEstadoSolicitud(new EstadoSolicitud(1, SolicitudStatus.POR_CALIFICAR.getNombre()));
        Arrendador arrendador = new Arrendador();
        arrendador.setIdCuenta(1);
        Propiedad propiedad = new Propiedad();
        propiedad.setIdPropiedad(1);
        propiedad.setArrendador(arrendador);
        solicitud.setPropiedad(propiedad);

        Arrendatario arrendatario = new Arrendatario();
        arrendatario.setIdCuenta(2);
        solicitud.setArrendatario(arrendatario);
    }
}
