package com.dreamteam.arriendatufinca.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PropiedadDTO {
    private Integer id_propiedad;
    private Integer id_arrendador;
    private String nombreArrendador;
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
}