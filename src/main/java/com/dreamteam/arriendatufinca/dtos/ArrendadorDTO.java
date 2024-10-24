package com.dreamteam.arriendatufinca.dtos;

import java.util.List;

import com.dreamteam.arriendatufinca.dtos.propiedad.BasePropiedadDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArrendadorDTO extends CuentaDTO{
    private List<BasePropiedadDTO> propiedades;
    public ArrendadorDTO(){
        super();
    }
    public ArrendadorDTO(Integer idCuenta, String nombreCuenta, String email, String apellidoCuenta, String telefono){
        super(idCuenta, nombreCuenta, email, apellidoCuenta, telefono);
    }
}
