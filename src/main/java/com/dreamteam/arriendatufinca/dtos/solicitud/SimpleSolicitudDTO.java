package com.dreamteam.arriendatufinca.dtos.solicitud;

import com.dreamteam.arriendatufinca.dtos.CuentaDTO;
import com.dreamteam.arriendatufinca.dtos.propiedad.SimplePropiedadDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SimpleSolicitudDTO extends BaseSolicitudDTO {
    private CuentaDTO arrendatario;
    private SimplePropiedadDTO propiedad;
}
