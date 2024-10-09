package com.dreamteam.arriendatufinca.dtos.propiedad;

import java.util.List;

import com.dreamteam.arriendatufinca.dtos.CuentaDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PropiedadDTO extends BasePropiedadDTO {
    private CuentaDTO arrendador;
    private List<SimplePropiedadDTO> solicitudes;
}