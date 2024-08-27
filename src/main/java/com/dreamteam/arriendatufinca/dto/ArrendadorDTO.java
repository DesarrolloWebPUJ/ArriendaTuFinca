package com.dreamteam.arriendatufinca.dto;

public class ArrendadorDTO extends CuentaDTO{
    public ArrendadorDTO(){
        super();
    }
    public ArrendadorDTO(Integer id_cuenta, String nombreCuenta, String email){
        this.id_cuenta = id_cuenta;
        this.nombreCuenta = nombreCuenta;
        this.email = email;
    }
}
