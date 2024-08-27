package com.dreamteam.arriendatufinca.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dreamteam.arriendatufinca.dto.CuentaDTO;
import com.dreamteam.arriendatufinca.services.CuentaService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping(value= "api/cuenta")
public class CuentaController {
    @Autowired
    CuentaService cuentaService;
    

    @CrossOrigin
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getCuenta(@PathVariable Integer id) {
        return cuentaService.get(id);
    }

    @CrossOrigin
    @GetMapping(value = "/email/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getCuenta(@PathVariable String email) {
        return cuentaService.get(email);
    }
    
    @CrossOrigin
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<CuentaDTO> getCuentas() {
        return cuentaService.get();
    }

    @CrossOrigin
    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateCuenta(@RequestBody CuentaDTO cuentaDTO) {
        return cuentaService.update(cuentaDTO);
    }

    @CrossOrigin
    @PutMapping(value = "/nuevaContrasena", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateContrasena(@RequestBody CuentaDTO cuentaDTO, @RequestParam String contrasenaIngresada, @RequestParam String nuevaContrasena) {
        return cuentaService.updateContrasena(cuentaDTO, contrasenaIngresada, nuevaContrasena);
    }

    @CrossOrigin
    @DeleteMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteCuenta(@RequestBody CuentaDTO cuentaDTO) {
        return cuentaService.deleteCuenta(cuentaDTO);
    }
}
