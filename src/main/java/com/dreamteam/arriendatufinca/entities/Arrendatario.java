package com.dreamteam.arriendatufinca.entities;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "Arrendatario")
@PrimaryKeyJoinColumn(name = "id_cuenta")
public class Arrendatario extends Cuenta{

    @OneToMany(mappedBy = "arrendatario")
    private List<Solicitud> solicitudes;
    
    public Arrendatario(){
        super();
    }

    public Arrendatario(String nombreCuenta, String contrasena, String email){
        super(nombreCuenta, contrasena, email);
    }
}
