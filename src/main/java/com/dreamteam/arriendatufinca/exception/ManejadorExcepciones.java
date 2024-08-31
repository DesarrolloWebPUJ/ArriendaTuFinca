package com.dreamteam.arriendatufinca.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import java.util.Calendar;

@ControllerAdvice
public class ManejadorExcepciones {

    private MensajeError crearMensajeError(ErrorResponseException ex) {
        // Crea un mensaje de error con la descripción de la excepción y la fecha actual
        return new MensajeError(ex.getBody().getDetail(), Calendar.getInstance().getTime(), ex.getStatusCode().value(), ex.getStatusCode().toString());
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<MensajeError> handleResponseStatusException(ResponseStatusException ex) {
        MensajeError error = crearMensajeError(ex);
        return ResponseEntity.status(ex.getStatusCode()).body(error);
    }
}