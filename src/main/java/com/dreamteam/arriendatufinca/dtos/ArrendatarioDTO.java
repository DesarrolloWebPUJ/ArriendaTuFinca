package com.dreamteam.arriendatufinca.dtos;

import java.util.List;

import com.dreamteam.arriendatufinca.dtos.solicitud.BaseSolicitudDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArrendatarioDTO extends CuentaDTO{
    private List<BaseSolicitudDTO> solicitudes;
    public ArrendatarioDTO(){
        super();
    }
    public ArrendatarioDTO(Integer idCuenta, String nombreCuenta, String email, String apellidoCuenta, String telefono){
        super(idCuenta, nombreCuenta, email, apellidoCuenta, telefono);
    }
}
