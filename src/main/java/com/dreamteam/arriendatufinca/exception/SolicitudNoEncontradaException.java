package com.dreamteam.arriendatufinca.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class SolicitudNoEncontradaException extends ResponseStatusException {

    public SolicitudNoEncontradaException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}