package com.dreamteam.arriendatufinca.dtos.propiedad;

import com.dreamteam.arriendatufinca.dtos.CuentaDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SimplePropiedadDTO extends BasePropiedadDTO {
    private CuentaDTO arrendador;
}
