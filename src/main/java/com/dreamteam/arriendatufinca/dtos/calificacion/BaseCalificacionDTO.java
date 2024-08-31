package com.dreamteam.arriendatufinca.dtos.calificacion;

import com.dreamteam.arriendatufinca.dtos.CuentaDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BaseCalificacionDTO {
    private Integer idCalificacion;
    private CuentaDTO calificador;
    private CuentaDTO calificado;
    private Integer puntaje;
    private String comentario;
    private String tipoCalificacion;
}
