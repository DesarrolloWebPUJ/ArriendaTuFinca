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
    Integer id_solicitud;
    Date fecha_inicio;
    Date fecha_final;
    Integer cantidad_personas;
    Integer id_arrendatario;
    Integer id_propiedad;
    Integer id_estado_solicitud;
}
