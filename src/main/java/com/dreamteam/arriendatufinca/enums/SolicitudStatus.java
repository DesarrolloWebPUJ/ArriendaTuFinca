package com.dreamteam.arriendatufinca.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum SolicitudStatus {
    PENDIENTE(1, "Pendiente"),
    POR_PAGAR(2, "Por pagar"),
    RECHAZADA(3, "Rechazada"),
    POR_CALIFICAR(4, "Por calificar"),
    CERRADA(5, "Cerrada");

    private String nombre;
    private Integer id;


    SolicitudStatus(Integer id, String value){
        this.nombre = value;
        this.id = id;
    }

    @JsonValue
    public String getNombre(){
        return nombre;
    }

    public Integer getId(){
        return id;
    }
}
