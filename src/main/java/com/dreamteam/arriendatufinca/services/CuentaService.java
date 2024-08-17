package com.dreamteam.arriendatufinca.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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

    public CuentaDTO get(Long Id){
        Optional<Cuenta> cuenta = cuentaRepository.findById(Id);
        CuentaDTO cuentaDTO = null;
        if (cuenta.isPresent()) {
            Cuenta cuentaTemp = cuenta.get();
            cuentaDTO = modelMapper.map(cuentaTemp, CuentaDTO.class);
        }
        return cuentaDTO;
    }

    public CuentaDTO get(String nombre){
        Optional<Cuenta> cuenta = cuentaRepository.findByNombre(nombre);
        CuentaDTO cuentaDTO = null;
        if (cuenta.isPresent()) {
            Cuenta cuentaTemp = cuenta.get();
            cuentaDTO = modelMapper.map(cuentaTemp, CuentaDTO.class);
        }
        return cuentaDTO;
    }

    public CuentaDTO saveNew(CuentaDTO cuentaDTO){
        Cuenta cuenta = modelMapper.map(cuentaDTO, Cuenta.class);
        cuenta.setEstado(Estado.ACTIVE);
        cuenta = cuentaRepository.save(cuenta); //Actualiza u obtiene el ID
        cuentaDTO = modelMapper.map(cuenta, CuentaDTO.class);
        return cuentaDTO;
    }

    public CuentaDTO update(CuentaDTO cuentaDTO){
        Cuenta cuenta = modelMapper.map(cuentaDTO, Cuenta.class);
        cuenta.setEstado(Estado.ACTIVE);
        cuenta = cuentaRepository.save(cuenta); //Actualiza u obtiene el ID
        cuentaDTO = modelMapper.map(cuenta, CuentaDTO.class);
        return cuentaDTO;
    }

    public void delete(CuentaDTO cuentaDTO){
        Cuenta cuenta = modelMapper.map(cuentaDTO, Cuenta.class);
        cuentaRepository.delete(cuenta);
    }
}
