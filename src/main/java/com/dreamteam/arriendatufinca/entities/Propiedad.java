package com.dreamteam.arriendatufinca.entities;

import java.util.List;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.dreamteam.arriendatufinca.enums.Estado;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
@SQLDelete(sql = "UPDATE propiedad SET estado = 0 WHERE id=?")
public class Propiedad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer idPropiedad;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_arrendador")
    private Arrendador arrendador;

    @OneToMany
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_propiedad")
    private List<Solicitud> solicitudes;

    @OneToMany(mappedBy = "propiedad", cascade = CascadeType.ALL)
    private List<Foto> fotos;

    private String nombrePropiedad;
    private String descripcionPropiedad;
    private String municipio;
    private String departamento;
    private String tipoIngreso;
    private Integer cantidadHabitaciones;
    private Integer cantidadBanos;
    private Boolean permiteMascotas;
    private Boolean tienePiscina;
    private Boolean tieneAsador;
    private Float valorNoche;
    private Estado estado;
    private Float puntajePromedio;
    private Integer cantidadCalificaciones;
}
