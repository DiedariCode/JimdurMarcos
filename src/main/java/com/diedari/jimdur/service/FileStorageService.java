package com.diedari.jimdur.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.diedari.jimdur.config.FileStorageProperties;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FileStorageService {

    private final FileStorageProperties fileStorageProperties;

    public String guardarImagenProducto(MultipartFile file) {
        try {
            String nombreArchivo = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            Path dirPath = Paths.get(fileStorageProperties.getUploadDir(), fileStorageProperties.getProductImagesDir());
            Path filePath = dirPath.resolve(nombreArchivo);
            
            Files.createDirectories(dirPath);
            Files.write(filePath, file.getBytes());

            return nombreArchivo;
        } catch (IOException e) {
            throw new RuntimeException("Error al guardar imagen: " + e.getMessage());
        }
    }

    public void eliminarImagenProducto(String nombreArchivo) {
        try {
            Path filePath = Paths.get(fileStorageProperties.getUploadDir(), 
                                    fileStorageProperties.getProductImagesDir(), 
                                    nombreArchivo);
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new RuntimeException("Error al eliminar imagen: " + e.getMessage());
        }
    }

    public String getProductImageUrl(String nombreArchivo) {
        return "/uploads/" + fileStorageProperties.getProductImagesDir() + "/" + nombreArchivo;
    }
} 