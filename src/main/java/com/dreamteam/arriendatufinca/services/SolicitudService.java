package com.dreamteam.arriendatufinca.services;

import com.dreamteam.arriendatufinca.dto.SolicitudDTO;
import com.dreamteam.arriendatufinca.entities.Solicitud;
import com.dreamteam.arriendatufinca.repository.SolicitudRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SolicitudService {

    @Autowired
    private SolicitudRepository solicitudRepository;

    @Autowired
    private ModelMapper modelMapper;

    public List<SolicitudDTO> getAllSolicitudes() {
        return solicitudRepository.findAll().stream()
                .map(solicitud -> modelMapper.map(solicitud, SolicitudDTO.class))
                .collect(Collectors.toList());
    }

    public SolicitudDTO getSolicitudById(Long id) {
        Optional<Solicitud> solicitud = solicitudRepository.findById(id);
        return solicitud.map(value -> modelMapper.map(value, SolicitudDTO.class)).orElse(null);
    }

    public SolicitudDTO saveSolicitud(SolicitudDTO solicitudDTO) {
        Solicitud solicitud = modelMapper.map(solicitudDTO, Solicitud.class);
        solicitud = solicitudRepository.save(solicitud);
        return modelMapper.map(solicitud, SolicitudDTO.class);
    }

    public void deleteSolicitud(Long id) {
        solicitudRepository.deleteById(id);
    }
}
