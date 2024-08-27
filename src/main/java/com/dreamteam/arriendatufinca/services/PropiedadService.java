package com.dreamteam.arriendatufinca.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.dreamteam.arriendatufinca.dto.PropiedadDTO;
import com.dreamteam.arriendatufinca.entities.Arrendador;
import com.dreamteam.arriendatufinca.entities.Estado;
import com.dreamteam.arriendatufinca.entities.Propiedad;
import com.dreamteam.arriendatufinca.repository.ArrendadorRepository;
import com.dreamteam.arriendatufinca.repository.PropiedadRepository;

@Service
public class PropiedadService {
    @Autowired
    private PropiedadRepository propiedadRepository;

    @Autowired
    private ArrendadorRepository arrendadorRepository;

    @Autowired
    private ModelMapper modelMapper;

    public ResponseEntity<?> saveNewPropiedad(PropiedadDTO propiedad){
        Optional<Arrendador> arrendadorTmp = arrendadorRepository.findById(propiedad.getId_arrendador());
        if (arrendadorTmp.isEmpty()) {
            return ResponseEntity.badRequest().body("No se encontro el arrendador");
        }
        Arrendador arrendador = arrendadorTmp.get();
        Propiedad newPropiedad = modelMapper.map(propiedad, Propiedad.class);
        newPropiedad.setArrendador(arrendador);
        newPropiedad.setEstado(Estado.ACTIVE);
        propiedadRepository.save(newPropiedad);
        propiedad.setId_propiedad(newPropiedad.getId_propiedad());
        propiedad.setNombreArrendador(arrendador.getNombreCuenta());
        propiedad.setEstado(newPropiedad.getEstado().getValue());
        return ResponseEntity.ok(propiedad);
    }

    public List<PropiedadDTO> getPropiedades(){
        List<Propiedad> propiedades = (List<Propiedad>) propiedadRepository.findAll();
        List<PropiedadDTO> propiedadesDTO = propiedades.stream()
                                                    .map(propiedad -> modelMapper.map(propiedad, PropiedadDTO.class))
                                                    .collect(Collectors.toList());
        return propiedadesDTO;
    }

    public ResponseEntity<?> getPropiedad(Integer id){
        Optional<Propiedad> propiedad = propiedadRepository.findById(id);
        if (propiedad.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        PropiedadDTO propiedadDTO = modelMapper.map(propiedad.get(), PropiedadDTO.class);
        return ResponseEntity.ok(propiedadDTO);
    }

    public ResponseEntity<?> desactivarPropiedad(PropiedadDTO propiedadDTO){
        Optional<Propiedad> propiedadTmp = propiedadRepository.findById(propiedadDTO.getId_propiedad());
        if (propiedadTmp.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Propiedad propiedad = propiedadTmp.get();
        propiedad.setEstado(Estado.INACTIVE);
        propiedadRepository.save(propiedad);
        propiedadDTO.setEstado(Estado.INACTIVE.getValue());
        return ResponseEntity.ok(propiedadDTO);
    }
}
