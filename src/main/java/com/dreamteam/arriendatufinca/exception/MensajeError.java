package com.dreamteam.arriendatufinca.exception;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MensajeError {
    private String message;
    private Date timestamp;
    private Integer status;
    private String reason;
}
