package com.dreamteam.arriendatufinca.controllers;

import com.dreamteam.arriendatufinca.dtos.EstadoSolicitudDTO;
import com.dreamteam.arriendatufinca.dtos.solicitud.SolicitudDTO;
import com.dreamteam.arriendatufinca.dtos.solicitud.SimpleSolicitudDTO;
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

    @CrossOrigin
    @GetMapping
    public List<SimpleSolicitudDTO> getAllSolicitudes() {
        return solicitudService.getAllSolicitudes();
    }

    @CrossOrigin
    @GetMapping("/{id}")
    public ResponseEntity<SolicitudDTO> getSolicitudById(@PathVariable Integer id) {
        return solicitudService.getSolicitudById(id);
    }

    @CrossOrigin
    @GetMapping("/arrendador/{arrendadorId}")
    public List<SolicitudDTO> getTopRecentSolicitudesByArrendadorId(
            @PathVariable int arrendadorId,
            @RequestParam int limit) {

        if (limit <= 0) {
            return solicitudService.getSolicitudesByArrendador(arrendadorId);
        } else {
            return solicitudService.getTopRecentSolicitudes(arrendadorId, limit);
        }
    }

    @CrossOrigin
    @GetMapping("/arrendatario/{arrendatarioId}")
    public List<SolicitudDTO> getSolicitudesByArrendatario(@PathVariable int arrendatarioId) {
        return solicitudService.getSolicitudesByArrendatario(arrendatarioId);
    }

    @CrossOrigin
    @PostMapping
    public ResponseEntity<SolicitudDTO> createSolicitud(@RequestBody SolicitudDTO solicitudDTO) {
        return solicitudService.saveSolicitud(solicitudDTO);
    }

    @CrossOrigin
    @PutMapping("/aprobar/{id}")
    public ResponseEntity<SolicitudDTO> aprobarSolicitud(@PathVariable Integer id){
        EstadoSolicitudDTO estadoSolicitudDTO = crearEstadoSolicitudDTO(SolicitudStatus.POR_PAGAR);
        return solicitudService.actualizarEstadoSolicitud(estadoSolicitudDTO, id);
    }

    @CrossOrigin
    @PutMapping("/rechazar/{id}")
    public ResponseEntity<SolicitudDTO> rechazarSolicitud(@PathVariable Integer id){
        EstadoSolicitudDTO estadoSolicitudDTO = crearEstadoSolicitudDTO(SolicitudStatus.RECHAZADA);
        return solicitudService.actualizarEstadoSolicitud(estadoSolicitudDTO, id);
    }

    @CrossOrigin
    @PutMapping("/pagar/{id}")
    public ResponseEntity<SolicitudDTO> pagarSolicitud(@PathVariable Integer id){
        EstadoSolicitudDTO estadoSolicitudDTO = crearEstadoSolicitudDTO(SolicitudStatus.POR_CALIFICAR);
        return solicitudService.actualizarEstadoSolicitud(estadoSolicitudDTO, id);
    }

    @CrossOrigin
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSolicitud(@PathVariable Integer id) {
        solicitudService.deleteSolicitud(id);
        return ResponseEntity.noContent().build();
    }

    private EstadoSolicitudDTO crearEstadoSolicitudDTO(SolicitudStatus solicitudStatus){
        return new EstadoSolicitudDTO(solicitudStatus.getId(), solicitudStatus.getNombre());
    }
}
