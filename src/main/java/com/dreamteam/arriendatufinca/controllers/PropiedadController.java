package com.dreamteam.arriendatufinca.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dreamteam.arriendatufinca.services.PropiedadService;
import com.dreamteam.arriendatufinca.dto.PropiedadDTO;

@RestController
@RequestMapping(value = "api/propiedad")
public class PropiedadController {
    @Autowired
    private PropiedadService propiedadService;

    @CrossOrigin
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<PropiedadDTO> getPropiedades() {
        return propiedadService.getPropiedades();
    }

    @CrossOrigin
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getPropiedad(@PathVariable Integer id) {
        return propiedadService.getPropiedad(id);
    }

    @CrossOrigin
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveNewPropiedad(@RequestBody PropiedadDTO propiedad) {
        return propiedadService.saveNewPropiedad(propiedad);
    }

    @CrossOrigin
    @DeleteMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deletePropiedad(@RequestBody PropiedadDTO propiedad) {
        return propiedadService.desactivarPropiedad(propiedad);
    }

}
