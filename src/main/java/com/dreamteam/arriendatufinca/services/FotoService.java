package com.dreamteam.arriendatufinca.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.dreamteam.arriendatufinca.entities.Foto;
import com.dreamteam.arriendatufinca.entities.Propiedad;
import com.dreamteam.arriendatufinca.repository.FotoRepository;
import com.dreamteam.arriendatufinca.repository.PropiedadRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class FotoService {
    private final FotoRepository fotoRepository;
    private final PropiedadRepository propiedadRepository;

    public static final String UPLOAD_DIR = "uploads/";

    public void savePhoto(Integer propiedadId, MultipartFile file) {
        Optional<Propiedad> propiedadOpt = propiedadRepository.findById(propiedadId);
        if (propiedadOpt.isEmpty()) {
            throw new RuntimeException("Propiedad no encontrada");
        }

        Propiedad propiedad = propiedadOpt.get();

        try {
            // Crear el directorio si no existe
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            byte[] bytes = file.getBytes();
            Path path = uploadPath.resolve(file.getOriginalFilename());
            Files.write(path, bytes);

            Foto foto = new Foto();
            foto.setUrl(file.getOriginalFilename());
            foto.setPropiedad(propiedad);
            fotoRepository.save(foto);
        } catch (IOException e) {
            throw new RuntimeException("Error al guardar la foto", e);
        }
    }

    public List<String> getFotosUrlByPropiedadId(Integer propiedadId) {
        Optional<Propiedad> propiedadOpt = propiedadRepository.findById(propiedadId);
        if (propiedadOpt.isEmpty()) {
            throw new RuntimeException("Propiedad no encontrada");
        }
        List<Foto> fotos = fotoRepository.findByPropiedadIdPropiedad(propiedadId);
        return fotos.stream().map(Foto::getUrl).toList();
    }
}