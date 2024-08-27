package com.dreamteam.arriendatufinca.entities;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Where(clause = "estado = 1")
@SQLDelete(sql = "UPDATE cuenta SET estado = 0 WHERE id=?")
public class Propiedad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id_propiedad;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_arrendador")
    private Arrendador arrendador;

    String nombrePropiedad;
    String descripcionPropiedad;
    String municipio;
    String departamento;
    String tipoIngreso;
    Integer cantidadHabitaciones;
    Integer cantidadBanos;
    Boolean permiteMascotas;
    Boolean tienePiscina;
    Boolean tieneAsador;
    Float valorNoche;
    Estado estado;
    Float puntajePromedio;

}
