package com.dreamteam.arriendatufinca.services;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

class UtilityServiceTest {

    @Test
    void testDevolverBadRequest() {
        String errorMessage = "Bad Request Error";
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            UtilityService.devolverBadRequest(errorMessage);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals(errorMessage, exception.getReason());
    }

    @Test
    void testDevolverNotFound() {
        String errorMessage = "Not Found Error";
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            UtilityService.devolverNotFound(errorMessage);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals(errorMessage, exception.getReason());
    }

    @Test
    void testDevolverUnauthorized() {
        String errorMessage = "Unauthorized Error";
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            UtilityService.devolverUnuthorized(errorMessage);
        });

        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatusCode());
        assertEquals(errorMessage, exception.getReason());
    }

    @Test
    void testVerificarExistencia_EntityPresent() {
        Optional<String> entity = Optional.of("Entity");
        String errorMessage = "Entity already exists";

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            UtilityService.verificarExistencia(entity, errorMessage);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals(errorMessage, exception.getReason());
    }

    @Test
    void testVerificarExistencia_EntityAbsent() {
        Optional<String> entity = Optional.empty();
        String errorMessage = "Entity already exists";

        assertDoesNotThrow(() -> {
            UtilityService.verificarExistencia(entity, errorMessage);
        });
    }

    @Test
    void testVerificarAusencia_EntityPresent() {
        Optional<String> entity = Optional.of("Entity");
        String errorMessage = "Entity not found";

        assertDoesNotThrow(() -> {
            UtilityService.verificarAusencia(entity, errorMessage);
        });
    }

    @Test
    void testVerificarAusencia_EntityAbsent() {
        Optional<String> entity = Optional.empty();
        String errorMessage = "Entity not found";

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            UtilityService.verificarAusencia(entity, errorMessage);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals(errorMessage, exception.getReason());
    }
}
