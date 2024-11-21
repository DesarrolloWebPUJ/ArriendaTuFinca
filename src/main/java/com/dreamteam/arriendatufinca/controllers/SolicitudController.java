package com.dreamteam.arriendatufinca.controllers;

import com.dreamteam.arriendatufinca.dtos.EstadoSolicitudDTO;
import com.dreamteam.arriendatufinca.dtos.solicitud.SolicitudDTO;
import com.dreamteam.arriendatufinca.dtos.solicitud.SimpleSolicitudDTO;
import com.dreamteam.arriendatufinca.enums.SolicitudStatus;
import com.dreamteam.arriendatufinca.services.JwtService;
import com.dreamteam.arriendatufinca.services.SolicitudService;

import lombok.AllArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("api/solicitud")
public class SolicitudController {
    private final SolicitudService solicitudService;
    private final JwtService jwtService;
    

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
    public ResponseEntity<SolicitudDTO> createSolicitud(Authentication authentication, @RequestBody SolicitudDTO solicitudDTO) {
        jwtService.verifyLoggedUser(solicitudDTO.getArrendatario(), authentication.getName());
        return solicitudService.saveSolicitud(solicitudDTO);
    }

    @CrossOrigin
    @PutMapping("/aprobar/{id}")
    public ResponseEntity<SolicitudDTO> aprobarSolicitud(Authentication authentication, @PathVariable Integer id){
        EstadoSolicitudDTO estadoSolicitudDTO = crearEstadoSolicitudDTO(SolicitudStatus.POR_PAGAR);
        return solicitudService.actualizarEstadoSolicitud(estadoSolicitudDTO, id, authentication.getName());
    }

    @CrossOrigin
    @PutMapping("/rechazar/{id}")
    public ResponseEntity<SolicitudDTO> rechazarSolicitud(Authentication authentication, @PathVariable Integer id){
        EstadoSolicitudDTO estadoSolicitudDTO = crearEstadoSolicitudDTO(SolicitudStatus.RECHAZADA);
        return solicitudService.actualizarEstadoSolicitud(estadoSolicitudDTO, id, authentication.getName());
    }

    @CrossOrigin
    @PutMapping("/pagar/{id}")
    public ResponseEntity<SolicitudDTO> pagarSolicitud(Authentication authentication, @PathVariable Integer id){
        EstadoSolicitudDTO estadoSolicitudDTO = crearEstadoSolicitudDTO(SolicitudStatus.POR_CALIFICAR);
        return solicitudService.actualizarEstadoSolicitud(estadoSolicitudDTO, id, authentication.getName());
    }

    @CrossOrigin
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSolicitud(Authentication authentication, @PathVariable Integer id) {
        solicitudService.deleteSolicitud(id, authentication.getName());
        return ResponseEntity.noContent().build();
    }

    private EstadoSolicitudDTO crearEstadoSolicitudDTO(SolicitudStatus solicitudStatus){
        return new EstadoSolicitudDTO(solicitudStatus.getId(), solicitudStatus.getNombre());
    }
}
