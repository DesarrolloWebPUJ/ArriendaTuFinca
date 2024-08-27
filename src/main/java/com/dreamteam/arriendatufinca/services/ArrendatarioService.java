package com.dreamteam.arriendatufinca.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.dreamteam.arriendatufinca.dto.ArrendatarioDTO;
import com.dreamteam.arriendatufinca.entities.Arrendatario;
import com.dreamteam.arriendatufinca.entities.Estado;
import com.dreamteam.arriendatufinca.repository.ArrendatarioRepository;

@Service
public class ArrendatarioService {
    @Autowired
    ArrendatarioRepository arrendatarioRepository;

    @Autowired
    ModelMapper modelMapper;

    public List<ArrendatarioDTO> getArrendatarios(){
        List<Arrendatario> arrendatarios = (List<Arrendatario>) arrendatarioRepository.findAll();
        List<ArrendatarioDTO> arrendatariosDTO = arrendatarios.stream()
                                                    .map(arrendatario -> modelMapper.map(arrendatario, ArrendatarioDTO.class))
                                                    .collect(Collectors.toList());
        return arrendatariosDTO;
    }

    public ResponseEntity<?> getArrendatario(Integer Id){
        Optional<Arrendatario> arrendatario = arrendatarioRepository.findById(Id);
        if (arrendatario.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        ArrendatarioDTO arrendatarioDTO = modelMapper.map(arrendatario.get(), ArrendatarioDTO.class);
        return ResponseEntity.ok(arrendatarioDTO);
    }

    public ResponseEntity<?> saveNewArrendatario(Arrendatario arrendatario){
        Optional<Arrendatario> arrendatarioTemp = arrendatarioRepository.findByEmail(arrendatario.getEmail());
        if (arrendatarioTemp.isPresent()) {
            return ResponseEntity.badRequest().body("Ya existe un arrendatario con ese correo");
        }
        arrendatario.setEstado(Estado.ACTIVE);
        Arrendatario savedArrendatario = arrendatarioRepository.save(arrendatario);
        ArrendatarioDTO arrendatarioDTO = modelMapper.map(savedArrendatario, ArrendatarioDTO.class);
        return ResponseEntity.ok(arrendatarioDTO);
    }
}
