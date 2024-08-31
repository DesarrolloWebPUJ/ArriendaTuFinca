package com.dreamteam.arriendatufinca.dtos.calificacion;

import com.dreamteam.arriendatufinca.dtos.propiedad.BasePropiedadDTO;
import com.dreamteam.arriendatufinca.dtos.solicitud.BaseSolicitudDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CalificacionDTO extends BaseCalificacionDTO {
    private BasePropiedadDTO propiedad;
    private BaseSolicitudDTO solicitud;
}