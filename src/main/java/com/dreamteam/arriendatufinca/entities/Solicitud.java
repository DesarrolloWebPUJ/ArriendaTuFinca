package com.dreamteam.arriendatufinca.entities;

import java.time.LocalDateTime;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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
public class Solicitud {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idSolicitud;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_arrendatario")
    private Arrendatario arrendatario;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_propiedad")
    private Propiedad propiedad;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_estado_solicitud")
    private EstadoSolicitud estadoSolicitud;

    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFinal;
    private LocalDateTime fechaCreacion;

    private Integer cantidadPersonas;
    private boolean arrendadorCalificado;
    private boolean arrendatarioCalificado;
    private boolean propiedadCalificado;
}
