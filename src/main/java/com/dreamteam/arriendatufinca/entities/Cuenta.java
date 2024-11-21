package com.dreamteam.arriendatufinca.entities;

import java.util.Collection;
import java.util.Collections;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.dreamteam.arriendatufinca.enums.Estado;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Where(clause = "estado = 1")
@SQLDelete(sql = "UPDATE cuenta SET estado = 0 WHERE id_cuenta=?")
@Table(name = "Cuenta")
@Inheritance(strategy = InheritanceType.JOINED)
public class Cuenta implements UserDetails{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idCuenta;
    private String nombreCuenta;
    private String apellidoCuenta;
    private String telefono;
    private String contrasena;
    private String email;
    private Estado estado;

    public Cuenta(String nombreCuenta, String contrasena, String email, String apellidoCuenta, String telefono){
        this.nombreCuenta = nombreCuenta;
        this.contrasena = contrasena;
        this.email = email;
        this.apellidoCuenta = apellidoCuenta;
        this.telefono = telefono;
        this.estado = Estado.ACTIVE;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Devuelve una colección de GrantedAuthority (roles o permisos)
        return Collections.emptyList(); // Aquí puedes agregar los roles o permisos del usuario
    }

    @Override
    public String getPassword() {
        return contrasena;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
