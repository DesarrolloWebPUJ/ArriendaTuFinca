package com.dreamteam.arriendatufinca.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.dreamteam.arriendatufinca.dtos.ArrendadorDTO;
import com.dreamteam.arriendatufinca.dtos.CuentaDTO;
import com.dreamteam.arriendatufinca.dtos.validation.SignUpRequest;
import com.dreamteam.arriendatufinca.entities.Arrendador;
import com.dreamteam.arriendatufinca.enums.Estado;
import com.dreamteam.arriendatufinca.exception.ManejadorErrores;
import com.dreamteam.arriendatufinca.repository.ArrendadorRepository;

@Service
public class ArrendadorService {
    private final ArrendadorRepository arrendadorRepository;
    private final ModelMapper modelMapper;

    public ArrendadorService(ArrendadorRepository arrendadorRepository, ModelMapper modelMapper) {
        this.arrendadorRepository = arrendadorRepository;
        this.modelMapper = modelMapper;
    }

    public List<CuentaDTO> getArrendadores(){
        List<Arrendador> arrendadores = (List<Arrendador>) arrendadorRepository.findAll();
        return arrendadores.stream().map(arrendador -> modelMapper.map(arrendador, CuentaDTO.class))
                                    .collect(Collectors.toList());
    }

    public ResponseEntity<ArrendadorDTO> getArrendador(Integer id){
        Optional<Arrendador> arrendadorTemp = arrendadorRepository.findById(id);
        UtilityService.verificarAusencia(arrendadorTemp, ManejadorErrores.ERROR_ARRENDADOR_NO_EXISTE);

        ArrendadorDTO arrendadorDTO = modelMapper.map(arrendadorTemp.get(), ArrendadorDTO.class);
        return ResponseEntity.ok(arrendadorDTO);
    }

    public ResponseEntity<CuentaDTO> saveNewArrendador(SignUpRequest arrendador){
        Optional<Arrendador> arrendadorTemp = arrendadorRepository.findByEmail(arrendador.getCuenta().getEmail());
        UtilityService.verificarExistencia(arrendadorTemp, ManejadorErrores.ERROR_CORREO_ARRENDADOR_YA_EXISTE);

        Arrendador newArrendador = modelMapper.map(arrendador.getCuenta(), Arrendador.class);
        newArrendador.setContrasena(arrendador.getContrasena());
        newArrendador.setEstado(Estado.ACTIVE);
        newArrendador = arrendadorRepository.save(newArrendador);
        arrendador.getCuenta().setIdCuenta(newArrendador.getIdCuenta());
        return ResponseEntity.ok(arrendador.getCuenta());
    }
}