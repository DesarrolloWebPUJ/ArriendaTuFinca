package com.dreamteam.arriendatufinca.dto;

public class ArrendatarioDTO extends CuentaDTO{
    public ArrendatarioDTO(){
        super();
    }
    public ArrendatarioDTO(Integer id_cuenta, String nombreCuenta, String email){
        this.id_cuenta = id_cuenta;
        this.nombreCuenta = nombreCuenta;
        this.email = email;
    }
}
