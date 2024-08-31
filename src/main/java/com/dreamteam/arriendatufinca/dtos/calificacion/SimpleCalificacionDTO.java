package com.dreamteam.arriendatufinca.dtos.calificacion;


import com.dreamteam.arriendatufinca.dtos.propiedad.SimplePropiedadDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SimpleCalificacionDTO extends BaseCalificacionDTO {
    private SimplePropiedadDTO propiedad;
}
