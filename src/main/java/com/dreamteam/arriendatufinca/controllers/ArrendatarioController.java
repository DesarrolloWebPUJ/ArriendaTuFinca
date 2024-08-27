package com.dreamteam.arriendatufinca.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dreamteam.arriendatufinca.dto.ArrendatarioDTO;
import com.dreamteam.arriendatufinca.entities.Arrendatario;
import com.dreamteam.arriendatufinca.services.ArrendatarioService;

@RestController
@RequestMapping(value = "api/arrendatario")
public class ArrendatarioController {
    @Autowired
    ArrendatarioService arrendatarioService;

    @CrossOrigin
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ArrendatarioDTO> getArrendatarios() {
        return arrendatarioService.getArrendatarios();
    }

    @CrossOrigin
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getArrendatario(@PathVariable Integer id) {
        return arrendatarioService.getArrendatario(id);
    }

    @CrossOrigin
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveNewArrendatario(@RequestBody Arrendatario arrendatario) {
        return arrendatarioService.saveNewArrendatario(arrendatario);
    }
}
