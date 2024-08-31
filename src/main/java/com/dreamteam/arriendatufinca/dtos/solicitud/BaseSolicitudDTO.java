package com.dreamteam.arriendatufinca.dtos.solicitud;

import java.util.Date;

import com.dreamteam.arriendatufinca.dtos.EstadoSolicitudDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BaseSolicitudDTO {
    private Integer idSolicitud;
    private Date fechaInicio;
    private Date fechaFinal;
    private Integer cantidadPersonas;
    private Date fechaCreacion;
    private boolean arrendadorCalificado;
    private boolean arrendatarioCalificado;
    private boolean propiedadCalificado;
    private EstadoSolicitudDTO estadoSolicitud;
}
