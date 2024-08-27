package com.dreamteam.arriendatufinca.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.dreamteam.arriendatufinca.dto.CuentaDTO;
import com.dreamteam.arriendatufinca.entities.Cuenta;
import com.dreamteam.arriendatufinca.entities.Estado;
import com.dreamteam.arriendatufinca.repository.CuentaRepository;

@Service
public class CuentaService {
    @Autowired
    CuentaRepository cuentaRepository;

    @Autowired
    ModelMapper modelMapper;

    public List<CuentaDTO> get(){
        List<Cuenta> cuentas = (List<Cuenta>) cuentaRepository.findAll();
        List<CuentaDTO> cuentasDTO = cuentas.stream()
                                                    .map(cuenta -> modelMapper.map(cuenta, CuentaDTO.class))
                                                    .collect(Collectors.toList());
        return cuentasDTO;
    }

    public ResponseEntity<?> get(Integer Id){
        Optional<Cuenta> cuenta = cuentaRepository.findById(Id);
        if (cuenta.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        CuentaDTO cuentaDTO = modelMapper.map(cuenta.get(), CuentaDTO.class);
        return ResponseEntity.ok(cuentaDTO);
    }

    public ResponseEntity<?> get(String email){
        Optional<Cuenta> cuenta = cuentaRepository.findByEmail(email);
        if (cuenta.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        CuentaDTO cuentaDTO = modelMapper.map(cuenta.get(), CuentaDTO.class);
        return ResponseEntity.ok(cuentaDTO);
    }

    public ResponseEntity<?> update(CuentaDTO cuentaDTO){
        Optional<Cuenta> optionalCuenta = cuentaRepository.findById(cuentaDTO.getId_cuenta());
        if (optionalCuenta.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Cuenta cuenta = optionalCuenta.get();
        cuenta.setNombreCuenta(cuentaDTO.getNombreCuenta());
        cuenta.setEstado(Estado.ACTIVE);
        cuenta = cuentaRepository.save(cuenta); //Actualiza u obtiene el ID
        cuentaDTO = modelMapper.map(cuenta, CuentaDTO.class);
        return ResponseEntity.ok(cuentaDTO);
    }

    public ResponseEntity<?> updateContrasena(CuentaDTO cuentaDTO, String contrasenaIngresada, String nuevaContrasena){
        Optional<Cuenta> optionalCuenta = cuentaRepository.findById(cuentaDTO.getId_cuenta());
        if (optionalCuenta.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Cuenta cuenta = optionalCuenta.get();
        if (cuenta.getContrasena().equals(contrasenaIngresada)) {
            cuenta.setContrasena(nuevaContrasena);
            cuenta = cuentaRepository.save(cuenta); //Actualiza u obtiene el ID
            cuentaDTO = modelMapper.map(cuenta, CuentaDTO.class);
            return ResponseEntity.ok(cuentaDTO);
        }
        else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Contraseña incorrecta");
        }
    }

    public ResponseEntity<?> deleteCuenta(CuentaDTO cuentaDTO){
        Optional<Cuenta> optionalCuenta = cuentaRepository.findById(cuentaDTO.getId_cuenta());
        if (optionalCuenta.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Cuenta cuenta = optionalCuenta.get();
        cuenta.setEstado(Estado.INACTIVE);
        cuenta = cuentaRepository.save(cuenta); //Actualiza u obtiene el ID
        cuentaDTO = modelMapper.map(cuenta, CuentaDTO.class);
        return ResponseEntity.ok(cuentaDTO);
    }
}
