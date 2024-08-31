package com.dreamteam.arriendatufinca.dtos.propiedad;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BasePropiedadDTO {
    private Integer idPropiedad;
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
    private String estado;
    private Float puntajePromedio;
    private Integer cantidadCalificaciones;

    public void actualizarPuntaje(Integer puntaje) {
        if (puntajePromedio == null) {
            puntajePromedio = puntaje.floatValue();
            cantidadCalificaciones = 1;
        } else {
            puntajePromedio = (puntajePromedio * cantidadCalificaciones + puntaje) / (cantidadCalificaciones + 1);
            cantidadCalificaciones++;
        }
    }
}
