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

import com.dreamteam.arriendatufinca.dtos.ArrendadorDTO;
import com.dreamteam.arriendatufinca.dtos.CuentaDTO;
import com.dreamteam.arriendatufinca.dtos.validation.SignUpRequest;
import com.dreamteam.arriendatufinca.services.ArrendadorService;
import com.dreamteam.arriendatufinca.services.CuentaService;

@RestController
@RequestMapping(value = "api/arrendador")
public class ArrendadorController {
    private final ArrendadorService arrendadorService;
    private final CuentaService cuentaService;

    public ArrendadorController(ArrendadorService arrendadorService, CuentaService cuentaService) {
        this.arrendadorService = arrendadorService;
        this.cuentaService = cuentaService;
    }

    @CrossOrigin
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<CuentaDTO> getArrendadores() {
        return arrendadorService.getArrendadores();
    }

    @CrossOrigin
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ArrendadorDTO> getArrendador(@PathVariable Integer id) {
        return arrendadorService.getArrendador(id);
    }

    @CrossOrigin
    @PostMapping(value = "/signup", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CuentaDTO> saveNewArrendador(@RequestBody SignUpRequest arrendador) {
        return arrendadorService.saveNewArrendador(arrendador);
    }

    @CrossOrigin
    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CuentaDTO> updateArrendador(@RequestBody CuentaDTO arrendador) {
        return cuentaService.update(arrendador);
    }

    @CrossOrigin
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteArrendador(@PathVariable Integer id) {
        cuentaService.deleteCuenta(id);
        return ResponseEntity.noContent().build();
    }

}
