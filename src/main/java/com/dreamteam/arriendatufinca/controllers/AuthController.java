package com.dreamteam.arriendatufinca.controllers;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dreamteam.arriendatufinca.dtos.validation.LoginRequest;
import com.dreamteam.arriendatufinca.entities.Cuenta;
import com.dreamteam.arriendatufinca.services.CuentaService;
import com.dreamteam.arriendatufinca.services.JwtService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value= "api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final CuentaService cuentaService;

    @CrossOrigin
    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> login(@RequestBody LoginRequest loginData) throws Exception {
        try {
            Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginData.getEmail(), loginData.getContrasena())
            );

            if (authentication.isAuthenticated()){
                Cuenta user = (Cuenta) authentication.getPrincipal();
                String rol = cuentaService.getCuentaRol(user);
                return ResponseEntity.ok(jwtService.generateToken(user, rol));
            }else{
                throw new Exception("Error en la autenticación");
            }
        } catch (Exception e) {
            throw new Exception("Error en la autenticación" + e.getMessage());
        }
    }
}
