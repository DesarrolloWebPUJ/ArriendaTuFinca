package com.dreamteam.arriendatufinca.controllers;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dreamteam.arriendatufinca.dtos.PagoDTO;
import com.dreamteam.arriendatufinca.services.PagoService;

@RestController
@RequestMapping(value = "api/pago")
public class PagoController {
    private final PagoService pagoService;

    public PagoController(PagoService pagoService) {
        this.pagoService = pagoService;
    }

    @CrossOrigin
    @GetMapping
    public List<PagoDTO> getPagos() {
        return pagoService.getPagos();
    }

    @CrossOrigin
    @GetMapping("/{id}")
    public ResponseEntity<PagoDTO> getPagoId(@PathVariable Integer id) {
        return pagoService.getPagoId(id);
    }

    @CrossOrigin
    @PostMapping
    public ResponseEntity<PagoDTO> createPago(@RequestBody PagoDTO pagoDTO) {
        return pagoService.saveNewPago(pagoDTO);
    }
}
