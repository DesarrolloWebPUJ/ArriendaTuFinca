package com.dreamteam.arriendatufinca.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CuentaDTO {
    private Integer idCuenta;
    private String nombreCuenta;
    private String email;
}
