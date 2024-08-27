package com.dreamteam.arriendatufinca.entities;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "Arrendador")
@PrimaryKeyJoinColumn(name = "id_cuenta")
public class Arrendador extends Cuenta{

    @OneToMany(mappedBy = "arrendador", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Propiedad> propiedades;
    
    public Arrendador(){
        super();
    }

    public Arrendador(String nombreCuenta, String contrasena, String email){
        this.nombreCuenta = nombreCuenta;
        this.contrasena = contrasena;
        this.email = email;
        this.estado = Estado.ACTIVE;
    }
}
