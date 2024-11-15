package com.dreamteam.arriendatufinca.services;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.dreamteam.arriendatufinca.dtos.CuentaDTO;
import com.dreamteam.arriendatufinca.dtos.propiedad.PropiedadDTO;
import com.dreamteam.arriendatufinca.dtos.propiedad.SimplePropiedadDTO;
import com.dreamteam.arriendatufinca.entities.Arrendador;
import com.dreamteam.arriendatufinca.entities.Propiedad;
import com.dreamteam.arriendatufinca.enums.Estado;
import com.dreamteam.arriendatufinca.exception.ManejadorErrores;
import com.dreamteam.arriendatufinca.repository.ArrendadorRepository;
import com.dreamteam.arriendatufinca.repository.PropiedadRepository;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import jakarta.annotation.PostConstruct;

@Service
public class PropiedadService {
    private final PropiedadRepository propiedadRepository;
    private final ArrendadorRepository arrendadorRepository;
    private final ModelMapper modelMapper;

    private Map<String, List<String>> departamentosMunicipios;

    public PropiedadService(PropiedadRepository propiedadRepository, ArrendadorRepository arrendadorRepository, ModelMapper modelMapper) {
        this.propiedadRepository = propiedadRepository;
        this.arrendadorRepository = arrendadorRepository;
        this.modelMapper = modelMapper;
    }

    @PostConstruct
    public void init() throws IOException, CsvException {
        departamentosMunicipios = new HashMap<>();
        ClassPathResource resource = new ClassPathResource("departamentos_y_municipios.csv");
        try (CSVReader reader = new CSVReader(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
            List<String[]> lines = reader.readAll();
            // Saltar la primera línea (cabecera)
            lines.remove(0);
            for (String[] line : lines) {
                String departamento = line[2]; // Columna "DEPARTAMENTO"
                String municipio = line[4];    // Columna "MUNICIPIO"
                departamentosMunicipios.computeIfAbsent(departamento, k -> new ArrayList<>()).add(municipio);
            }
        }
    }

    public ResponseEntity<SimplePropiedadDTO> saveNewPropiedad(SimplePropiedadDTO propiedadDTO) {
        Optional<Arrendador> arrendadorTmp = arrendadorRepository.findById(propiedadDTO.getArrendador().getIdCuenta());
        // Verificar los datos de la propiedad
        verificarDatosPropiedad(propiedadDTO, arrendadorTmp);
        
        Arrendador arrendador = arrendadorTmp.get();

        // Mappear la propiedad
        propiedadDTO.setPuntajePromedio(null);
        Propiedad newPropiedad = modelMapper.map(propiedadDTO, Propiedad.class);

        // Guardar la propiedad y mapear la respuesta
        newPropiedad.setArrendador(arrendador);
        newPropiedad.setEstado(Estado.ACTIVE);
        newPropiedad.setCantidadCalificaciones(0);
        newPropiedad.setPuntajePromedio(null);
        newPropiedad = propiedadRepository.save(newPropiedad);
        propiedadDTO = modelMapper.map(newPropiedad, SimplePropiedadDTO.class);

        return ResponseEntity.ok(propiedadDTO);
    }

    public ResponseEntity<SimplePropiedadDTO> updatePropiedad(SimplePropiedadDTO propiedadDTO){
        // Verificar que la propiedad exista y coincida
        Propiedad propiedad = verificarPropiedadExistente(propiedadDTO, propiedadDTO.getArrendador());

        // Actualizar la propiedad
        Propiedad newPropiedad = modelMapper.map(propiedadDTO, Propiedad.class);
        newPropiedad.setEstado(propiedad.getEstado());
        newPropiedad.setSolicitudes(propiedad.getSolicitudes());
        newPropiedad.setCantidadCalificaciones(propiedad.getCantidadCalificaciones());
        newPropiedad.setPuntajePromedio(propiedad.getPuntajePromedio());
        newPropiedad = propiedadRepository.save(newPropiedad);

        propiedadDTO = modelMapper.map(newPropiedad, SimplePropiedadDTO.class);
        return ResponseEntity.ok(propiedadDTO);
    }

    private Propiedad verificarPropiedadExistente(SimplePropiedadDTO propiedadDTO, CuentaDTO arrendadorDTO) {
        Optional<Propiedad> propiedadTmp = propiedadRepository.findById(propiedadDTO.getIdPropiedad());
        UtilityService.verificarAusencia(propiedadTmp, ManejadorErrores.ERROR_PROPIEDAD_NO_EXISTE);

        Propiedad propiedad = propiedadTmp.get();
        Optional<Arrendador> arrendadorTmp = arrendadorRepository.findById(propiedad.getArrendador().getIdCuenta());
        verificarDatosPropiedad(propiedadDTO, arrendadorTmp);
        
        Arrendador arrendador = arrendadorTmp.get();
        if (!arrendador.getIdCuenta().equals(arrendadorDTO.getIdCuenta())) {
            UtilityService.devolverBadRequest(ManejadorErrores.ERROR_ARRENDADOR_INCORRECTO);
        }
        return propiedad;
    }

    private void verificarDatosPropiedad(SimplePropiedadDTO propiedad, Optional<Arrendador> arrendadorTmp) {
        // Verificar que el arrendador exista
        UtilityService.verificarAusencia(arrendadorTmp, ManejadorErrores.ERROR_ARRENDADOR_NO_EXISTE);

        // Verificar que el municipio y departamento sean válidos
        if (!verificarMunicipioYDepartamento(propiedad.getMunicipio(), propiedad.getDepartamento())) {
           UtilityService.devolverBadRequest(ManejadorErrores.ERROR_MUNICIPIO_O_DEPARTAMENTO_INVALIDO);
        }
    }

    public List<PropiedadDTO> getPropiedades(){
        List<Propiedad> propiedades = (List<Propiedad>) propiedadRepository.findAll();
        return propiedades.stream().map(propiedad -> modelMapper.map(propiedad, PropiedadDTO.class))
                                    .collect(Collectors.toList());
    }

    public ResponseEntity<PropiedadDTO> getPropiedad(Integer id){
        Optional<Propiedad> propiedad = propiedadRepository.findById(id);
        UtilityService.verificarAusencia(propiedad, ManejadorErrores.ERROR_PROPIEDAD_NO_EXISTE);

        PropiedadDTO propiedadDTO = modelMapper.map(propiedad.get(), PropiedadDTO.class);
        return ResponseEntity.ok(propiedadDTO);
    }

    public List<SimplePropiedadDTO> getPropiedadesByArrendador(Integer idArrendador){
        Optional<Arrendador> arrendadorTmp = arrendadorRepository.findById(idArrendador);
        UtilityService.verificarAusencia(arrendadorTmp, ManejadorErrores.ERROR_ARRENDADOR_NO_EXISTE);

        Arrendador arrendador = arrendadorTmp.get();
        List<Propiedad> propiedades = arrendador.getPropiedades();
        return propiedades.stream().map(propiedad -> modelMapper.map(propiedad, SimplePropiedadDTO.class))
                                    .collect(Collectors.toList());
    }

    public void desactivarPropiedad(Integer id, String emailAutenticado){
        // Verificar que la propiedad exista
        Optional<Propiedad> propiedadTmp = propiedadRepository.findById(id);
        UtilityService.verificarAusencia(propiedadTmp, ManejadorErrores.ERROR_PROPIEDAD_NO_EXISTE);
        Propiedad propiedad = propiedadTmp.get();
        
        if(!propiedad.getArrendador().getEmail().equals(emailAutenticado)){
            UtilityService.devolverUnuthorized(ManejadorErrores.ERROR_ARRENDADOR_INCORRECTO);
        }

        // Desactivar la propiedad
        propiedad.setEstado(Estado.INACTIVE);
        propiedadRepository.save(propiedad);
    }

    public List<String> getDepartamentos(){
        return new ArrayList<>(departamentosMunicipios.keySet());
    }

    public List<String> getMunicipiosByDepartamento(String departamento){
        return departamentosMunicipios.get(departamento);
    }

    private Boolean verificarMunicipioYDepartamento(String municipio, String departamento){
        List<String> municipios = departamentosMunicipios.get(departamento);
        if (municipios == null) {
            return false;
        }
        return municipios.contains(municipio);
    }
}
