package com.dreamteam.arriendatufinca.controllers;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dreamteam.arriendatufinca.dtos.propiedad.PropiedadDTO;
import com.dreamteam.arriendatufinca.dtos.propiedad.SimplePropiedadDTO;
import com.dreamteam.arriendatufinca.services.PropiedadService;

@RestController
@RequestMapping(value = "api/propiedad")
public class PropiedadController {
    private final PropiedadService propiedadService;

    public PropiedadController(PropiedadService propiedadService) {
        this.propiedadService = propiedadService;
    }

    @CrossOrigin
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<PropiedadDTO> getPropiedades() {
        return propiedadService.getPropiedades();
    }

    @CrossOrigin
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PropiedadDTO> getPropiedad(@PathVariable Integer id) {
        return propiedadService.getPropiedad(id);
    }

    @CrossOrigin
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SimplePropiedadDTO> saveNewPropiedad(@RequestBody SimplePropiedadDTO propiedad) {
        return propiedadService.saveNewPropiedad(propiedad);
    }

    @CrossOrigin
    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SimplePropiedadDTO> updatePropiedad(@RequestBody SimplePropiedadDTO propiedad) {
        return propiedadService.updatePropiedad(propiedad);
    }

    @CrossOrigin
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deletePropiedad(@PathVariable Integer id) {
        propiedadService.desactivarPropiedad(id);
        return ResponseEntity.noContent().build();
    }

}
