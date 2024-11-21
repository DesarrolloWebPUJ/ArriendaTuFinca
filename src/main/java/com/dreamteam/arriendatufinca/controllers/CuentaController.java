package com.dreamteam.arriendatufinca.controllers;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dreamteam.arriendatufinca.dtos.CuentaDTO;
import com.dreamteam.arriendatufinca.services.CuentaService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping(value= "api/cuenta")
@RequiredArgsConstructor
public class CuentaController {
    private final CuentaService cuentaService;

    @CrossOrigin
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CuentaDTO> getCuenta(@PathVariable Integer id) {
        return cuentaService.get(id);
    }

    @CrossOrigin
    @GetMapping(value = "/email/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CuentaDTO> getCuenta(@PathVariable String email) {
        return cuentaService.get(email);
    }
    
    @CrossOrigin
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<CuentaDTO> getCuentas() {
        return cuentaService.get();
    }

    // @CrossOrigin
    // @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    // public ResponseEntity<String> login(@RequestBody LoginRequest loginData) throws Exception {
    //     Authentication authentication = authenticationManager.authenticate(
    //         new UsernamePasswordAuthenticationToken(loginData.getEmail(), loginData.getContrasena())
    //     );

    //     if (authentication.isAuthenticated()){
    //         Cuenta user = (Cuenta) authentication.getPrincipal();
    //         return ResponseEntity.ok(jwtService.generateToken(user));
    //     }else{
    //         throw new Exception("Error en la autenticación");
    //     }
    // }

    @CrossOrigin
    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CuentaDTO> updateCuenta(@RequestBody CuentaDTO cuentaDTO) {
        return cuentaService.update(cuentaDTO);
    }

    @CrossOrigin
    @PutMapping(value = "/nuevaContrasena", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CuentaDTO> updateContrasena(@RequestBody CuentaDTO cuentaDTO, @RequestParam String contrasenaIngresada, @RequestParam String nuevaContrasena) {
        return cuentaService.updateContrasena(cuentaDTO, contrasenaIngresada, nuevaContrasena);
    }

    @CrossOrigin
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteCuenta(@PathVariable Integer id) {
        cuentaService.deleteCuenta(id);
        return ResponseEntity.noContent().build();
    }
}
