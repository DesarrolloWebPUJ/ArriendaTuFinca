package com.dreamteam.arriendatufinca.enums;

public enum CuentaRol {
    ARRENDADOR( "Arrendador"),
    ARRENDATARIO( "Arrendatario");

    private String nombre;

    CuentaRol(String value){
        this.nombre = value;
    }

    public String getNombre(){
        return nombre;
    }

}
