package com.dreamteam.arriendatufinca.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CalificacionDTO {
    private Integer id_calificacion;
    private CuentaDTO calificado;
    private CuentaDTO calificador;
    private Integer id_propiedad;
    private Integer puntaje;
    private String comentario;
}