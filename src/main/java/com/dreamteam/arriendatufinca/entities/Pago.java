package com.dreamteam.arriendatufinca.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Pago {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPago;

    @ManyToOne
    private Solicitud solicitud; // Relación con la solicitud

    private String banco; // Nombre del banco
    private String numeroCuenta; // Número de cuenta bancaria
    private Double valorPago; // Monto a pagar
}