package com.dreamteam.arriendatufinca.exception;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Calendar;
import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

class ManejadorExcepcionesTest {

    private ManejadorExcepciones manejadorExcepciones;

    @BeforeEach
    void setUp() {
        manejadorExcepciones = new ManejadorExcepciones();
    }

    @Test
    void testHandleResponseStatusException() {
        String errorMessage = "Not Found Error";
        ResponseStatusException exception = new ResponseStatusException(HttpStatus.NOT_FOUND, errorMessage);

        ResponseEntity<MensajeError> responseEntity = manejadorExcepciones.handleResponseStatusException(exception);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        MensajeError error = responseEntity.getBody();
        assertNotNull(error);
        assertEquals(errorMessage, error.getMessage());
        assertEquals(HttpStatus.NOT_FOUND.value(), error.getStatus());
        assertEquals(HttpStatus.NOT_FOUND.toString(), error.getReason());
        assertTrue(isRecent(error.getTimestamp()));
    }

    private boolean isRecent(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, -5);
        Date fiveSecondsAgo = calendar.getTime();
        return date.after(fiveSecondsAgo);
    }
}