package com.dreamteam.arriendatufinca.controllers;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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
import com.dreamteam.arriendatufinca.services.JwtService;
import com.dreamteam.arriendatufinca.services.PropiedadService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping(value = "api/propiedad")
@AllArgsConstructor
public class PropiedadController {
    private final PropiedadService propiedadService;
    private final JwtService jwtService;

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
    @GetMapping(value = "/departamentos", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<String> getDepartamentos() {
        return propiedadService.getDepartamentos();
    }

    @CrossOrigin
    @GetMapping(value = "/{departamento}/municipios", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<String> getMunicipiosByDepartamento(@PathVariable String departamento){
        return propiedadService.getMunicipiosByDepartamento(departamento);
    }

    @CrossOrigin
    @GetMapping(value = "/arrendador/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<SimplePropiedadDTO> getPropiedadesByArrendador(@PathVariable Integer id) {
        return propiedadService.getPropiedadesByArrendador(id);
    }

    @CrossOrigin
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SimplePropiedadDTO> saveNewPropiedad(Authentication authentication, @RequestBody SimplePropiedadDTO propiedad) {
        jwtService.verifyLoggedUser(propiedad.getArrendador(), authentication.getName());
        return propiedadService.saveNewPropiedad(propiedad);
    }

    @CrossOrigin
    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SimplePropiedadDTO> updatePropiedad(Authentication authentication, @RequestBody SimplePropiedadDTO propiedad) {
        jwtService.verifyLoggedUser(propiedad.getArrendador(), authentication.getName());
        return propiedadService.updatePropiedad(propiedad);
    }

    @CrossOrigin
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deletePropiedad(Authentication authentication, @PathVariable Integer id) {
        propiedadService.desactivarPropiedad(id, authentication.getName());
        return ResponseEntity.noContent().build();
    }
}
