package com.dreamteam.arriendatufinca.services;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dreamteam.arriendatufinca.dtos.PagoDTO;
import com.dreamteam.arriendatufinca.entities.Pago;
import com.dreamteam.arriendatufinca.entities.Solicitud;
import com.dreamteam.arriendatufinca.exception.SolicitudNoEncontradaException;
import com.dreamteam.arriendatufinca.repository.PagoRepository;
import com.dreamteam.arriendatufinca.repository.SolicitudRepository;

@Service
public class PagoService {

    private final PagoRepository pagoRepository;
    private final SolicitudRepository solicitudRepository;

    public PagoService(PagoRepository pagoRepository, SolicitudRepository solicitudRepository) {
        this.pagoRepository = pagoRepository;
        this.solicitudRepository = solicitudRepository;
    }

    // Método para obtener todos los pagos
    public List<PagoDTO> getPagos() {
        return pagoRepository.findAll().stream()
                .map(this::convertirADTO)
                .toList();
    }

    public ResponseEntity<PagoDTO> getPagoId(Long id) {
        Pago pago = pagoRepository.findById(id)
                .orElseThrow(() -> new SolicitudNoEncontradaException("Pago no encontrado"));
        return ResponseEntity.ok(convertirADTO(pago));
    }
@Transactional
public ResponseEntity<PagoDTO> saveNewPago(PagoDTO pagoDTO) {
    // Validar que la solicitud existe usando el repository
    Solicitud solicitud = solicitudRepository.findById(pagoDTO.getSolicitudId())
            .orElseThrow(() -> new SolicitudNoEncontradaException("Solicitud no encontrada"));

    // Crear la entidad Pago
    Pago nuevoPago = new Pago();
    nuevoPago.setSolicitud(solicitud);
    nuevoPago.setBanco(pagoDTO.getBanco());
    nuevoPago.setNumeroCuenta(pagoDTO.getNumeroCuenta());
    nuevoPago.setValorPago(pagoDTO.getValor());

    // Guardar el nuevo pago en la base de datos
    Pago savedPago = pagoRepository.save(nuevoPago);
    return ResponseEntity.status(HttpStatus.CREATED).body(convertirADTO(savedPago));
}

    // Método para convertir Pago a PagoDTO
    private PagoDTO convertirADTO(Pago pago) {
        PagoDTO pagoDTO = new PagoDTO();
        pagoDTO.setSolicitudId(pago.getSolicitud().getIdSolicitud());
        pagoDTO.setBanco(pago.getBanco());
        pagoDTO.setNumeroCuenta(pago.getNumeroCuenta());
        pagoDTO.setValor(pago.getValorPago());
        return pagoDTO;
    }

    public ResponseEntity<PagoDTO> getPagoId(Integer id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getPagoId'");
    }
}
