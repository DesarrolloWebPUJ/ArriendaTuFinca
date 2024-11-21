package com.dreamteam.arriendatufinca.dtos.solicitud;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.dreamteam.arriendatufinca.dtos.EstadoSolicitudDTO;
import com.fasterxml.jackson.annotation.JsonFormat;

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

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaInicio;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaFinal;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private LocalDateTime fechaCreacion;

    private Integer cantidadPersonas;
    private float valor;
    private boolean arrendadorCalificado;
    private boolean arrendatarioCalificado;
    private boolean propiedadCalificado;
    private EstadoSolicitudDTO estadoSolicitud;
}
