package com.dreamteam.arriendatufinca.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.dreamteam.arriendatufinca.dtos.ArrendatarioDTO;
import com.dreamteam.arriendatufinca.dtos.CuentaDTO;
import com.dreamteam.arriendatufinca.dtos.validation.SignUpRequest;
import com.dreamteam.arriendatufinca.entities.Arrendatario;
import com.dreamteam.arriendatufinca.enums.Estado;
import com.dreamteam.arriendatufinca.exception.ManejadorErrores;
import com.dreamteam.arriendatufinca.repository.ArrendatarioRepository;

@Service
public class ArrendatarioService {
    private final ArrendatarioRepository arrendatarioRepository;
    private final ModelMapper modelMapper;

    public ArrendatarioService(ArrendatarioRepository arrendatarioRepository, ModelMapper modelMapper) {
        this.arrendatarioRepository = arrendatarioRepository;
        this.modelMapper = modelMapper;
    }

    public List<CuentaDTO> getArrendatarios(){
        List<Arrendatario> arrendatarios = (List<Arrendatario>) arrendatarioRepository.findAll();
        return arrendatarios.stream().map(arrendatario -> modelMapper.map(arrendatario, CuentaDTO.class))
                                     .collect(Collectors.toList());
    }

    public ResponseEntity<ArrendatarioDTO> getArrendatario(Integer id){
        Optional<Arrendatario> arrendatario = arrendatarioRepository.findById(id);
        UtilityService.verificarAusencia(arrendatario, ManejadorErrores.ERROR_ARRENDATARIO_NO_EXISTE);

        ArrendatarioDTO arrendatarioDTO = modelMapper.map(arrendatario.get(), ArrendatarioDTO.class);
        return ResponseEntity.ok(arrendatarioDTO);
    }

    public ResponseEntity<CuentaDTO> saveNewArrendatario(SignUpRequest arrendatario){
        Optional<Arrendatario> arrendatarioTemp = arrendatarioRepository.findByEmail(arrendatario.getCuenta().getEmail());
        UtilityService.verificarExistencia(arrendatarioTemp, ManejadorErrores.ERROR_CORREO_ARRENDATARIO_YA_EXISTE);

        Arrendatario newArrendatario = modelMapper.map(arrendatario.getCuenta(), Arrendatario.class);
        newArrendatario.setContrasena(arrendatario.getContrasena());
        newArrendatario.setEstado(Estado.ACTIVE);
        Arrendatario savedArrendatario = arrendatarioRepository.save(newArrendatario);
        arrendatario.getCuenta().setIdCuenta(savedArrendatario.getIdCuenta());
        return ResponseEntity.ok(arrendatario.getCuenta());
    }

}
