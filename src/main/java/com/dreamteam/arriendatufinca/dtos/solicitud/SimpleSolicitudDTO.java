package com.dreamteam.arriendatufinca.dtos.solicitud;

import com.dreamteam.arriendatufinca.dtos.CuentaDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SimpleSolicitudDTO extends BaseSolicitudDTO {
    private CuentaDTO arrendatario;
}
