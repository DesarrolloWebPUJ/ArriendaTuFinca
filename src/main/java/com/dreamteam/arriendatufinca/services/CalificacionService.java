package com.dreamteam.arriendatufinca.services;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.dreamteam.arriendatufinca.dtos.EstadoSolicitudDTO;
import com.dreamteam.arriendatufinca.dtos.calificacion.BaseCalificacionDTO;
import com.dreamteam.arriendatufinca.dtos.calificacion.CalificacionDTO;
import com.dreamteam.arriendatufinca.dtos.propiedad.SimplePropiedadDTO;
import com.dreamteam.arriendatufinca.dtos.solicitud.SolicitudDTO;
import com.dreamteam.arriendatufinca.entities.Calificacion;
import com.dreamteam.arriendatufinca.entities.Cuenta;
import com.dreamteam.arriendatufinca.entities.Propiedad;
import com.dreamteam.arriendatufinca.entities.Solicitud;
import com.dreamteam.arriendatufinca.enums.SolicitudStatus;
import com.dreamteam.arriendatufinca.enums.TipoCalificacion;
import com.dreamteam.arriendatufinca.exception.ManejadorErrores;
import com.dreamteam.arriendatufinca.repository.CalificacionRepository;
import com.dreamteam.arriendatufinca.repository.CuentaRepository;
import com.dreamteam.arriendatufinca.repository.PropiedadRepository;
import com.dreamteam.arriendatufinca.repository.SolicitudRepository;

@Service
public class CalificacionService {
    private final CalificacionRepository calificacionRepository;
    private final CuentaRepository cuentaRepository;
    private final SolicitudRepository solicitudRepository;
    private final PropiedadRepository propiedadRepository;
    private final SolicitudService solicitudService;
    private final PropiedadService propiedadService;
    private final ModelMapper modelMapper;

    public CalificacionService(CalificacionRepository calificacionRepository, CuentaRepository cuentaRepository,
                               SolicitudRepository solicitudRepository, PropiedadRepository propiedadRepository,
                               ModelMapper modelMapper, SolicitudService solicitudService, PropiedadService propiedadService) {
        this.calificacionRepository = calificacionRepository;
        this.cuentaRepository = cuentaRepository;
        this.solicitudRepository = solicitudRepository;
        this.propiedadRepository = propiedadRepository;
        this.modelMapper = modelMapper;
        this.solicitudService = solicitudService;
        this.propiedadService = propiedadService;
    }
    

    public List<CalificacionDTO> getCalificaciones(){
        List<Calificacion> calificaciones = (List<Calificacion>) calificacionRepository.findAll();
        return calificaciones.stream().map(calificacion -> modelMapper.map(calificacion, CalificacionDTO.class))
                                     .collect(Collectors.toList());
    }

    public ResponseEntity<CalificacionDTO> getCalificacionId(Integer id){
        Optional<Calificacion> calificacion = calificacionRepository.findById(id);
        UtilityService.verificarAusencia(calificacion, ManejadorErrores.ERROR_CALIFICACION_NO_EXISTE);

        CalificacionDTO calificacionDTO = modelMapper.map(calificacion.get(), CalificacionDTO.class);
        return ResponseEntity.ok(calificacionDTO);
    }

    public List<BaseCalificacionDTO> getCalificacionesCuenta(Integer id){
        Optional<Cuenta> cuenta = cuentaRepository.findById(id);
        UtilityService.verificarAusencia(cuenta, ManejadorErrores.ERROR_CUENTA_NO_EXISTE);

        List<Calificacion> calificaciones = calificacionRepository.findByIdCalificado(id);
        return calificaciones.stream().map(calificacion -> modelMapper.map(calificacion, BaseCalificacionDTO.class))
                                     .collect(Collectors.toList());
    }

    public ResponseEntity<CalificacionDTO> saveNewCalificacion(CalificacionDTO calificacionDTO){
        Calificacion calificacion = verificarCalificacion(calificacionDTO);

        SolicitudDTO solicitudDTO = modelMapper.map(calificacion.getSolicitud(), SolicitudDTO.class);
        

        solicitudDTO = asignarCalificacionSolicitud(calificacionDTO, solicitudDTO);
        solicitudService.updateSolicitud(solicitudDTO);
        calificacion.setTipoCalificacion(TipoCalificacion.fromString(calificacionDTO.getTipoCalificacion()));

        calificacion = calificacionRepository.save(calificacion);
        calificacionDTO = modelMapper.map(calificacion, CalificacionDTO.class);
        return ResponseEntity.ok(calificacionDTO);
    }

    private Calificacion verificarCalificacion(CalificacionDTO calificacion){
        Optional<Cuenta> calificador = cuentaRepository.findById(calificacion.getCalificador().getIdCuenta());
        UtilityService.verificarAusencia(calificador, ManejadorErrores.ERROR_CUENTA_NO_EXISTE);

        Optional<Cuenta> calificado = cuentaRepository.findById(calificacion.getCalificado().getIdCuenta());
        UtilityService.verificarAusencia(calificado, ManejadorErrores.ERROR_CUENTA_NO_EXISTE);

        Optional<Propiedad> propiedad = propiedadRepository.findById(calificacion.getPropiedad().getIdPropiedad());
        UtilityService.verificarAusencia(propiedad, ManejadorErrores.ERROR_PROPIEDAD_NO_EXISTE);

        Optional<Solicitud> solicitudTmp = solicitudRepository.findById(calificacion.getSolicitud().getIdSolicitud());
        UtilityService.verificarAusencia(solicitudTmp, ManejadorErrores.ERROR_SOLICITUD_NO_EXISTE);

        Solicitud solicitud = solicitudTmp.get();
        if (!solicitud.getEstadoSolicitud().getNombreEstadoSolicitud().equals(SolicitudStatus.POR_CALIFICAR.getNombre())){
            UtilityService.devolverBadRequest(ManejadorErrores.ERROR_SOLICITUD_NO_PUEDE_SER_CALIFICADA);
        }
        verificarEntidades(solicitud, calificacion);
        if (calificacion.getPuntaje() < 0 || calificacion.getPuntaje() > 5) {
            UtilityService.devolverBadRequest(ManejadorErrores.ERROR_PUNTAJE_INVALIDO);
        }
        Calificacion newCalificacion = modelMapper.map(calificacion, Calificacion.class);
        newCalificacion.setPropiedad(propiedad.get());
        newCalificacion.setCalificador(calificador.get());
        newCalificacion.setCalificado(calificado.get());
        newCalificacion.setSolicitud(solicitud);
        return newCalificacion;
    }

    private void verificarEntidades(Solicitud solicitud, CalificacionDTO calificacionDTO){
        Integer idCalificador = calificacionDTO.getCalificador().getIdCuenta();
        Integer idCalificado = calificacionDTO.getCalificado().getIdCuenta();
        Integer idPropiedad = calificacionDTO.getPropiedad().getIdPropiedad();
        if (idCalificado.equals(idCalificador)){
            UtilityService.devolverBadRequest(ManejadorErrores.ERROR_CUENTA_REPETIDA);
        }
        else if (idCalificador != solicitud.getArrendatario().getIdCuenta() && idCalificador != solicitud.getPropiedad().getArrendador().getIdCuenta()){
            UtilityService.devolverBadRequest(ManejadorErrores.ERROR_CALIFICADOR_NO_PERTENECE_SOLICITUD);
        }
        else if (idCalificado != solicitud.getArrendatario().getIdCuenta() && idCalificado != solicitud.getPropiedad().getArrendador().getIdCuenta()){
            UtilityService.devolverBadRequest(ManejadorErrores.ERROR_CALIFICADO_NO_PERTENECE_SOLICITUD);
        }
        else if (idPropiedad != solicitud.getPropiedad().getIdPropiedad()){
            UtilityService.devolverBadRequest(ManejadorErrores.ERROR_PROPIEDAD_NO_PERTENECE_SOLICITUD);
        }
    }

    protected SolicitudDTO asignarCalificacionSolicitud(CalificacionDTO calificacionDTO, SolicitudDTO solicitudDTO){
        String tipoCalificacion = calificacionDTO.getTipoCalificacion();
        Solicitud solicitud = solicitudRepository.findById(solicitudDTO.getIdSolicitud()).get();
        if (tipoCalificacion.equals(TipoCalificacion.ARRENDADOR_A_ARRENDATARIO.getValue()) && !solicitud.isArrendatarioCalificado()) {
            solicitudDTO.setArrendatarioCalificado(true);
        }
        else if (tipoCalificacion.equals(TipoCalificacion.ARRENDATARIO_A_ARRENDADOR.getValue()) && !solicitud.isArrendadorCalificado()) {
            solicitudDTO.setArrendadorCalificado(true);
        }
        else if (tipoCalificacion.equals(TipoCalificacion.ARRENDATARIO_A_PROPIEDAD.getValue()) && !solicitud.isPropiedadCalificado()) {
            // Actualizar puntaje de la propiedad
            SimplePropiedadDTO propiedadDTO = solicitudDTO.getPropiedad();
            propiedadDTO.actualizarPuntaje(calificacionDTO.getPuntaje());
            propiedadService.updatePropiedad(propiedadDTO);

            solicitudDTO.setPropiedadCalificado(true);
        }
        else{
            UtilityService.devolverBadRequest(ManejadorErrores.ERROR_TIPO_CALIFICACION_INVALIDO);
        }

        // Verificar si la solicitud ya fue calificada en todos los aspectos
        if (solicitudDTO.isArrendatarioCalificado() && solicitudDTO.isArrendadorCalificado() && solicitudDTO.isPropiedadCalificado()) {
            SolicitudStatus solicitudStatus = SolicitudStatus.CERRADA;
            solicitudDTO.setEstadoSolicitud(new EstadoSolicitudDTO(solicitudStatus.getId(), solicitudStatus.getNombre()));
        }

        return solicitudDTO;
    }
}
