package com.dreamteam.arriendatufinca.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CuentaDTO {
    Long cuenta_id;
    String nombre;
    String correo;
}
