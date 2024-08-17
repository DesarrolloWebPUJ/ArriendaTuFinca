package com.dreamteam.arriendatufinca.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dreamteam.arriendatufinca.dto.CuentaDTO;
import com.dreamteam.arriendatufinca.services.CuentaService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping(value= "api/cuenta")
public class CuentaController {
    @Autowired
    CuentaService cuentaService;

    @CrossOrigin
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CuentaDTO getCuenta(@PathVariable Long id) {
        return cuentaService.get(id);
    }

    @CrossOrigin
    @GetMapping(value = "/nombre/{nombre}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CuentaDTO getCuenta(@PathVariable String nombre) {
        return cuentaService.get(nombre);
    }
    
    @CrossOrigin
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<CuentaDTO> getCuentas() {
        return cuentaService.get();
    }

    @CrossOrigin
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public CuentaDTO insertCuenta(@RequestBody CuentaDTO cuentaDTO) {
        return cuentaService.saveNew(cuentaDTO);
    }

    @CrossOrigin
    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public CuentaDTO updateCuenta(@RequestBody CuentaDTO cuentaDTO) {
        return cuentaService.update(cuentaDTO);
    }

    @CrossOrigin
    @DeleteMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public void deleteCuenta(@RequestBody CuentaDTO cuentaDTO) {
        cuentaService.delete(cuentaDTO);
    }
}
