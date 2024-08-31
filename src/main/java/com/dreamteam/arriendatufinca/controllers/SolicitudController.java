package com.dreamteam.arriendatufinca.controllers;

import com.dreamteam.arriendatufinca.dtos.EstadoSolicitudDTO;
import com.dreamteam.arriendatufinca.dtos.solicitud.SimpleSolicitudDTO;
import com.dreamteam.arriendatufinca.dtos.solicitud.SolicitudDTO;
import com.dreamteam.arriendatufinca.enums.SolicitudStatus;
import com.dreamteam.arriendatufinca.services.SolicitudService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/solicitud")
public class SolicitudController {
    private final SolicitudService solicitudService;

    public SolicitudController(SolicitudService solicitudService) {
        this.solicitudService = solicitudService;
    }

    @GetMapping
    public List<SolicitudDTO> getAllSolicitudes() {
        return solicitudService.getAllSolicitudes();
    }

    @GetMapping("/{id}")
    public ResponseEntity<SimpleSolicitudDTO> getSolicitudById(@PathVariable Integer id) {
        return solicitudService.getSolicitudById(id);
    }

    @PostMapping
    public ResponseEntity<SimpleSolicitudDTO> createSolicitud(@RequestBody SimpleSolicitudDTO solicitudDTO) {
        return solicitudService.saveSolicitud(solicitudDTO);
    }

    @PutMapping("/aprobar/{id}")
    public ResponseEntity<SimpleSolicitudDTO> aprobarSolicitud(@PathVariable Integer id){
        EstadoSolicitudDTO estadoSolicitudDTO = crearEstadoSolicitudDTO(SolicitudStatus.POR_PAGAR);
        return solicitudService.actualizarEstadoSolicitud(estadoSolicitudDTO, id);
    }
    @PutMapping("/rechazar/{id}")
    public ResponseEntity<SimpleSolicitudDTO> rechazarSolicitud(@PathVariable Integer id){
        EstadoSolicitudDTO estadoSolicitudDTO = crearEstadoSolicitudDTO(SolicitudStatus.RECHAZADA);
        return solicitudService.actualizarEstadoSolicitud(estadoSolicitudDTO, id);
    }
    @PutMapping("/pagar/{id}")
    public ResponseEntity<SimpleSolicitudDTO> pagarSolicitud(@PathVariable Integer id){
        EstadoSolicitudDTO estadoSolicitudDTO = crearEstadoSolicitudDTO(SolicitudStatus.POR_CALIFICAR);
        return solicitudService.actualizarEstadoSolicitud(estadoSolicitudDTO, id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSolicitud(@PathVariable Integer id) {
        solicitudService.deleteSolicitud(id);
        return ResponseEntity.noContent().build();
    }

    private EstadoSolicitudDTO crearEstadoSolicitudDTO(SolicitudStatus solicitudStatus){
        return new EstadoSolicitudDTO(solicitudStatus.getId(), solicitudStatus.getNombre());
    }
}
