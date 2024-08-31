package com.dreamteam.arriendatufinca.controllers;

import java.util.List;

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

import com.dreamteam.arriendatufinca.dtos.ArrendatarioDTO;
import com.dreamteam.arriendatufinca.dtos.CuentaDTO;
import com.dreamteam.arriendatufinca.services.ArrendatarioService;
import com.dreamteam.arriendatufinca.services.CuentaService;

@RestController
@RequestMapping(value = "api/arrendatario")
public class ArrendatarioController {
    private final ArrendatarioService arrendatarioService;
    private final CuentaService cuentaService;

    public ArrendatarioController(ArrendatarioService arrendatarioService, CuentaService cuentaService) {
        this.arrendatarioService = arrendatarioService;
        this.cuentaService = cuentaService;
    }

    @CrossOrigin
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<CuentaDTO> getArrendatarios() {
        return arrendatarioService.getArrendatarios();
    }

    @CrossOrigin
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ArrendatarioDTO> getArrendatario(@PathVariable Integer id) {
        return arrendatarioService.getArrendatario(id);
    }

    @CrossOrigin
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ArrendatarioDTO> saveNewArrendatario(@RequestBody ArrendatarioDTO arrendatario) {
        return arrendatarioService.saveNewArrendatario(arrendatario);
    }

    @CrossOrigin
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteArrendatario(@PathVariable Integer id) {
        cuentaService.deleteCuenta(id);
        return ResponseEntity.noContent().build();
    }
}
