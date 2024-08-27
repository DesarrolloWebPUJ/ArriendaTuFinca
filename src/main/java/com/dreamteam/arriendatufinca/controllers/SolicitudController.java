package com.dreamteam.arriendatufinca.controllers;

import com.dreamteam.arriendatufinca.dto.SolicitudDTO;
import com.dreamteam.arriendatufinca.services.SolicitudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/solicitud")
public class SolicitudController {

    @Autowired
    private SolicitudService solicitudService;

    @GetMapping
    public List<SolicitudDTO> getAllSolicitudes() {
        return solicitudService.getAllSolicitudes();
    }

    @GetMapping("/{id}")
    public ResponseEntity<SolicitudDTO> getSolicitudById(@PathVariable Integer id) {
        SolicitudDTO solicitudDTO = solicitudService.getSolicitudById(id);
        if (solicitudDTO != null) {
            return ResponseEntity.ok(solicitudDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> createSolicitud(@RequestBody SolicitudDTO solicitudDTO) {
        return solicitudService.saveSolicitud(solicitudDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSolicitud(@PathVariable Integer id) {
        solicitudService.deleteSolicitud(id);
        return ResponseEntity.noContent().build();
    }
}
