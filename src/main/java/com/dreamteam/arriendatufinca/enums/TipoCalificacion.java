package com.dreamteam.arriendatufinca.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum TipoCalificacion {
    ARRENDADOR_A_ARRENDATARIO("Arrendador a arrendatario"),
    ARRENDATARIO_A_ARRENDADOR("Arrendatario a arrendador"),
    ARRENDATARIO_A_PROPIEDAD("Arrendatario a propiedad");

    private String value;


    TipoCalificacion(String value){
        this.value = value;
    }

    @JsonValue
    public String getValue(){
        return value;
    }

    public static TipoCalificacion fromString(String value) {
        for (TipoCalificacion tipo : TipoCalificacion.values()) {
            if (tipo.getValue().equalsIgnoreCase(value)) {
                return tipo;
            }
        }
        throw new IllegalArgumentException("TipoCalificacion no válido: " + value);
    }
}
