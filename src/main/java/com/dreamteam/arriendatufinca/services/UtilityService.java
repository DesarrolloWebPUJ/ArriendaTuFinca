package com.dreamteam.arriendatufinca.services;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class UtilityService {

    private UtilityService(){
        throw new IllegalStateException("Utility class");
    }

    public static void devolverBadRequest(String errorMessage) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMessage);
    }

    public static void devolverNotFound(String errorMessage) {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, errorMessage);
    }

    public static void devolverUnuthorized(String errorMessage) {
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, errorMessage);
    }

    public static void verificarExistencia(Optional<?> entity, String errorMessage) {
        if (entity.isPresent()) {
            devolverBadRequest(errorMessage);
        }
    }

    public static void verificarAusencia(Optional<?> entity, String errorMessage) {
        if (entity.isEmpty()) {
            devolverNotFound(errorMessage);
        }
    }
}
