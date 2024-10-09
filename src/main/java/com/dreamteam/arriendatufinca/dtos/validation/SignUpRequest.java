package com.dreamteam.arriendatufinca.dtos.validation;

import com.dreamteam.arriendatufinca.dtos.CuentaDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequest {
    private CuentaDTO cuenta;
    private String contrasena;
}
