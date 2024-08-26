package com.dreamteam.arriendatufinca.dto;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SolicitudDTO {
    Long id_solicitud;
    Date fecha_inicio;
    Date fecha_final;
    Integer cantidad_personas;
    Long id_arrendatario;
    Long id_propiedad;
    Long id_estado_solicitud;
}
