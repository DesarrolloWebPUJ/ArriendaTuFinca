package com.dreamteam.arriendatufinca.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.dreamteam.arriendatufinca.dtos.CuentaDTO;
import com.dreamteam.arriendatufinca.entities.Cuenta;
import com.dreamteam.arriendatufinca.enums.CuentaRol;
import com.dreamteam.arriendatufinca.enums.Estado;
import com.dreamteam.arriendatufinca.exception.ManejadorErrores;
import com.dreamteam.arriendatufinca.repository.ArrendadorRepository;
import com.dreamteam.arriendatufinca.repository.CuentaRepository;

@Service
public class CuentaService implements UserDetailsService{
    private final CuentaRepository cuentaRepository;
    private final ArrendadorRepository arrendadorRepository;
    private final ModelMapper modelMapper;

    public CuentaService(CuentaRepository cuentaRepository, ModelMapper modelMapper, ArrendadorRepository arrendadorRepository) {
        this.cuentaRepository = cuentaRepository;
        this.modelMapper = modelMapper;
        this.arrendadorRepository = arrendadorRepository;
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

    public ResponseEntity<CuentaDTO> login(String email, String contrasena){
        Optional<Cuenta> cuentaTmp = cuentaRepository.findByEmail(email);
        UtilityService.verificarAusencia(cuentaTmp, ManejadorErrores.ERROR_CORREO_CUENTA_NO_EXISTE);
        Cuenta cuenta = cuentaTmp.get();
        if (!cuenta.getContrasena().equals(contrasena)) {
            UtilityService.devolverUnuthorized(ManejadorErrores.ERROR_CONTRASENA_INCORRECTA);
        }
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

    public String getCuentaRol(Cuenta cuenta){
        if (arrendadorRepository.findById(cuenta.getIdCuenta()).isPresent()) {
            return CuentaRol.ARRENDADOR.getNombre();
        }else{
            return CuentaRol.ARRENDATARIO.getNombre();
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Cuenta> cuentaTmp = cuentaRepository.findByEmail(email);
        UtilityService.verificarAusencia(cuentaTmp, ManejadorErrores.ERROR_CORREO_CUENTA_NO_EXISTE);
        Cuenta cuenta = cuentaTmp.get();
        return cuenta;
    }
}