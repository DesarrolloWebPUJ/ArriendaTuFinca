package com.dreamteam.arriendatufinca.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dreamteam.arriendatufinca.dtos.calificacion.BaseCalificacionDTO;
import com.dreamteam.arriendatufinca.dtos.calificacion.CalificacionDTO;
import com.dreamteam.arriendatufinca.services.CalificacionService;

@RestController
@RequestMapping(value = "api/calificacion")
public class CalificacionController {
    private final CalificacionService calificacionService;

    public CalificacionController(CalificacionService calificacionService) {
        this.calificacionService = calificacionService;
    }

    @GetMapping
    public List<CalificacionDTO> getCalificaciones() {
        return calificacionService.getCalificaciones();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CalificacionDTO> getCalificacionId(@PathVariable Integer id) {
        return calificacionService.getCalificacionId(id);
    }

    @GetMapping("/calificado/{id}")
    public List<BaseCalificacionDTO> getCalificacionCalificado(@PathVariable Integer id) {
        return calificacionService.getCalificacionesCuenta(id);
    }

    @PostMapping
    public ResponseEntity<CalificacionDTO> createCalificacion(@RequestBody CalificacionDTO calificacionDTO) {
        return calificacionService.saveNewCalificacion(calificacionDTO);
    }
}
