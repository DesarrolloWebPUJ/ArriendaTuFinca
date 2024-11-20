package com.dreamteam.arriendatufinca.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

    private static final String UPLOAD_DIR = "uploads/";

    public void savePhoto(Integer propiedadId, MultipartFile file) {
        Optional<Propiedad> propiedadOpt = propiedadRepository.findById(propiedadId);
        if (propiedadOpt.isEmpty()) {
            throw new RuntimeException("Propiedad no encontrada");
        }

        Propiedad propiedad = propiedadOpt.get();

        try {
            byte[] bytes = file.getBytes();
            Path path = Paths.get(UPLOAD_DIR + file.getOriginalFilename());
            Files.write(path, bytes);

            Foto foto = new Foto();
            foto.setUrl(path.toString());
            foto.setPropiedad(propiedad);
            fotoRepository.save(foto);
        } catch (IOException e) {
            throw new RuntimeException("Error al guardar la foto", e);
        }
    }
}
