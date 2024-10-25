package com.dreamteam.arriendatufinca.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PagoDTO {
    private Long solicitudId; // ID de la solicitud asociada al pago
    private String banco; // Nombre del banco
    private String numeroCuenta; // Número de cuenta bancaria
    private Double valor; // Monto a pagar
}