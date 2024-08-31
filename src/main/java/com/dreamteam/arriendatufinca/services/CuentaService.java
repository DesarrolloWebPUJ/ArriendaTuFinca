package com.dreamteam.arriendatufinca.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.dreamteam.arriendatufinca.dtos.CuentaDTO;
import com.dreamteam.arriendatufinca.entities.Cuenta;
import com.dreamteam.arriendatufinca.enums.Estado;
import com.dreamteam.arriendatufinca.exception.ManejadorErrores;
import com.dreamteam.arriendatufinca.repository.CuentaRepository;

@Service
public class CuentaService {
    private final CuentaRepository cuentaRepository;
    private final ModelMapper modelMapper;

    public CuentaService(CuentaRepository cuentaRepository, ModelMapper modelMapper) {
        this.cuentaRepository = cuentaRepository;
        this.modelMapper = modelMapper;
    }

    public List<CuentaDTO> get(){
        List<Cuenta> cuentas = (List<Cuenta>) cuentaRepository.findAll();
        return cuentas.stream().map(cuenta -> modelMapper.map(cuenta, CuentaDTO.class))
                               .collect(Collectors.toList());
    }

    public ResponseEntity<CuentaDTO> get(Integer id){
        Optional<Cuenta> cuentaTmp = cuentaRepository.findById(id);
        UtilityService.verificarAusencia(cuentaTmp, ManejadorErrores.ERROR_CUENTA_NO_EXISTE);
        Cuenta cuenta = cuentaTmp.get();
        CuentaDTO cuentaDTO = modelMapper.map(cuenta, CuentaDTO.class);
        return ResponseEntity.ok(cuentaDTO);
    }

    public ResponseEntity<CuentaDTO> get(String email){
        Optional<Cuenta> cuentaTmp = cuentaRepository.findByEmail(email);
        UtilityService.verificarAusencia(cuentaTmp, ManejadorErrores.ERROR_CORREO_CUENTA_NO_EXISTE);
        Cuenta cuenta = cuentaTmp.get();
        CuentaDTO cuentaDTO = modelMapper.map(cuenta, CuentaDTO.class);
        return ResponseEntity.ok(cuentaDTO);
    }

    public ResponseEntity<CuentaDTO> update(CuentaDTO cuentaDTO){
        Optional<Cuenta> optionalCuenta = cuentaRepository.findById(cuentaDTO.getIdCuenta());
        UtilityService.verificarAusencia(optionalCuenta, ManejadorErrores.ERROR_CUENTA_NO_EXISTE);

        Cuenta cuenta = optionalCuenta.get();
        cuenta.setNombreCuenta(cuentaDTO.getNombreCuenta());
        cuenta.setEstado(Estado.ACTIVE);
        cuenta = cuentaRepository.save(cuenta); //Actualiza u obtiene el ID
        cuentaDTO = modelMapper.map(cuenta, CuentaDTO.class);
        return ResponseEntity.ok(cuentaDTO);
    }

    public ResponseEntity<CuentaDTO> updateContrasena(CuentaDTO cuentaDTO, String contrasenaIngresada, String nuevaContrasena){
        Optional<Cuenta> optionalCuenta = cuentaRepository.findById(cuentaDTO.getIdCuenta());
        UtilityService.verificarAusencia(optionalCuenta, ManejadorErrores.ERROR_CUENTA_NO_EXISTE);

        Cuenta cuenta = optionalCuenta.get();
        if (!cuenta.getContrasena().equals(contrasenaIngresada)) {
            UtilityService.devolverUnuthorized(ManejadorErrores.ERROR_CONTRASENA_INCORRECTA);
        }
        cuenta.setContrasena(nuevaContrasena);
        cuenta = cuentaRepository.save(cuenta); //Actualiza u obtiene el ID
        cuentaDTO = modelMapper.map(cuenta, CuentaDTO.class);
        return ResponseEntity.ok(cuentaDTO);
    }

    public void deleteCuenta(Integer id){
        Optional<Cuenta> optionalCuenta = cuentaRepository.findById(id);
        UtilityService.verificarAusencia(optionalCuenta, ManejadorErrores.ERROR_CUENTA_NO_EXISTE);

        Cuenta cuenta = optionalCuenta.get();
        cuenta.setEstado(Estado.INACTIVE);
        cuentaRepository.save(cuenta); //Actualiza u obtiene el ID
    }
}