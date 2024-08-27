package com.dreamteam.arriendatufinca.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SolicitudDTO {
    private Integer id_solicitud;
    private Integer id_arrendatario;
    private Integer id_propiedad;
    private String fechaInicio;
    private String fechaFin;
    private String estadoSolicitud;
}
