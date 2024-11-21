package com.dreamteam.arriendatufinca.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dreamteam.arriendatufinca.dtos.calificacion.BaseCalificacionDTO;
import com.dreamteam.arriendatufinca.dtos.calificacion.CalificacionDTO;
import com.dreamteam.arriendatufinca.entities.Calificacion;
import com.dreamteam.arriendatufinca.services.CalificacionService;
import com.dreamteam.arriendatufinca.services.JwtService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping(value = "api/calificacion")
public class CalificacionController {
    private final CalificacionService calificacionService;
    private final JwtService jwtService;
    

    @CrossOrigin
    @GetMapping
    public List<CalificacionDTO> getCalificaciones() {
        return calificacionService.getCalificaciones();
    }

    @CrossOrigin
    @GetMapping("/{id}")
    public ResponseEntity<CalificacionDTO> getCalificacionId(@PathVariable Integer id) {
        return calificacionService.getCalificacionId(id);
    }

    @CrossOrigin
    @GetMapping("/calificado/{id}")
    public List<BaseCalificacionDTO> getCalificacionCalificado(@PathVariable Integer id) {
        return calificacionService.getCalificacionesCuenta(id);
    }

    @CrossOrigin
        @PostMapping("/submit")
    public ResponseEntity<?> submitCalificacion(@RequestBody Calificacion calificacion) {
        try {
            // Aquí, llamamos al servicio para procesar y guardar la calificación.
            calificacionService.submitCalificacion(calificacion);
            return ResponseEntity.ok("Calificación recibida con éxito.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Ocurrió un error al procesar la calificación.");
        }
    }
    
}
