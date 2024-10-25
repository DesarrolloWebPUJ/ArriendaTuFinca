package com.dreamteam.arriendatufinca.entities;



import org.springframework.data.annotation.Id;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Pago {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Solicitud solicitud; // Relación con la solicitud

    private String banco; // Nombre del banco
    private String numeroCuenta; // Número de cuenta bancaria
    private Double valor; // Monto a pagar
    public void setSolicitud(Pago solicitud2) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setSolicitud'");
    }
}