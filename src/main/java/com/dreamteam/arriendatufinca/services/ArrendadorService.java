package com.dreamteam.arriendatufinca.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.dreamteam.arriendatufinca.dto.ArrendadorDTO;
import com.dreamteam.arriendatufinca.entities.Arrendador;
import com.dreamteam.arriendatufinca.entities.Estado;
import com.dreamteam.arriendatufinca.repository.ArrendadorRepository;

@Service
public class ArrendadorService {
    @Autowired
    ArrendadorRepository arrendadorRepository;

    @Autowired
    ModelMapper modelMapper;

    public List<ArrendadorDTO> getArrendadores(){
        List<Arrendador> arrendadores = (List<Arrendador>) arrendadorRepository.findAll();
        List<ArrendadorDTO> arrendadoresDTO = arrendadores.stream()
                                                    .map(arrendador -> modelMapper.map(arrendador, ArrendadorDTO.class))
                                                    .collect(Collectors.toList());
        return arrendadoresDTO;
    }

    public ResponseEntity<?> getArrendador(Integer Id){
        Optional<Arrendador> arrendador = arrendadorRepository.findById(Id);
        if (arrendador.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        ArrendadorDTO arrendadorDTO = modelMapper.map(arrendador.get(), ArrendadorDTO.class);
        return ResponseEntity.ok(arrendadorDTO);
    }

    public ResponseEntity<?> saveNewArrendador(Arrendador arrendador){
        Optional<Arrendador> arrendadorTemp = arrendadorRepository.findByEmail(arrendador.getEmail());
        if (arrendadorTemp.isPresent()) {
            return ResponseEntity.badRequest().body("Ya existe un arrendador con ese correo");
        }
        arrendador.setEstado(Estado.ACTIVE);
        Arrendador savedArrendador = arrendadorRepository.save(arrendador);
        ArrendadorDTO arrendadorDTO = modelMapper.map(savedArrendador, ArrendadorDTO.class);
        return ResponseEntity.ok(arrendadorDTO);
    }
}
