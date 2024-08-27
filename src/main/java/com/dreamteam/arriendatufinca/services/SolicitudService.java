package com.dreamteam.arriendatufinca.services;

import com.dreamteam.arriendatufinca.dto.SolicitudDTO;
import com.dreamteam.arriendatufinca.entities.EstadoSolicitud;
import com.dreamteam.arriendatufinca.entities.Propiedad;
import com.dreamteam.arriendatufinca.entities.Solicitud;
import com.dreamteam.arriendatufinca.repository.PropiedadRepository;
import com.dreamteam.arriendatufinca.repository.SolicitudRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SolicitudService {

    @Autowired
    private SolicitudRepository solicitudRepository;

    @Autowired
    private PropiedadRepository propiedadRepository;

    @Autowired
    private ModelMapper modelMapper;

    public List<SolicitudDTO> getAllSolicitudes() {
        List<Solicitud> solicitudes = (List<Solicitud>) solicitudRepository.findAll();
        return solicitudes.stream()
                .map(solicitud -> modelMapper.map(solicitud, SolicitudDTO.class))
                .collect(Collectors.toList());
    }

    public SolicitudDTO getSolicitudById(Integer id) {
        Optional<Solicitud> solicitud = solicitudRepository.findById(id);
        return solicitud.map(value -> modelMapper.map(value, SolicitudDTO.class)).orElse(null);
    }

    public ResponseEntity<?> saveSolicitud(SolicitudDTO solicitudDTO) {
        Optional<Propiedad> propiedad = propiedadRepository.findById(solicitudDTO.getId_propiedad());
        if (propiedad.isEmpty()){
            return ResponseEntity.badRequest().body("No existe la propiedad");
        }
        // Verificar que la fecha inicial sea mayor a la fecha actual
        if (solicitudDTO.getFecha_inicio().before(new java.util.Date())){
            return ResponseEntity.badRequest().body("La fecha de inicio debe ser mayor a la fecha actual");
        }
        // Verificar que la fecha final sea mayor a la fecha de inicio
        else if (solicitudDTO.getFecha_final().before(solicitudDTO.getFecha_inicio())){
            return ResponseEntity.badRequest().body("La fecha final debe ser mayor a la fecha de inicio");
        }
        Solicitud solicitud = modelMapper.map(solicitudDTO, Solicitud.class);
        // EstadoSolicitud estadoSolicitud = new EstadoSolicitud();
        // estadoSolicitud.setId_estado_solicitud(0);
        // solicitud.setEstadoSolicitud(estadoSolicitud);
        solicitud = solicitudRepository.save(solicitud);
        return ResponseEntity.ok(solicitudDTO);
    }

    public void deleteSolicitud(Integer id) {
        solicitudRepository.deleteById(id);
    }
}
