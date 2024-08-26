package com.dreamteam.arriendatufinca.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "solicitud")
public class Solicitud {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id_solicitud;
    @Temporal(TemporalType.DATE)
    Date fecha_inicio;
    @Temporal(TemporalType.DATE)
    Date fecha_final;
    Integer cantidad_personas;
    /*@ManyToOne
    @JoinColumn(name = "id_arrendatario")
    Arrendatario arrendatario;
    @ManyToOne
    @JoinColumn(name = "id_propiedad")
    Propiedad propiedad;
    @ManyToOne
    @JoinColumn(name = "id_estado_solicitud")
    EstadoSolicitud estadoSolicitud;*/
}
