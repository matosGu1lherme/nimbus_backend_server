package com.nimbus.nimbusWebServer.services;

import com.nimbus.nimbusWebServer.exception.customException.FileNotFoundException;
import com.nimbus.nimbusWebServer.exception.customException.FileStorageException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class LocalStorageService implements StorageService{

    private final Path root = Paths.get("uploads");

    public LocalStorageService() throws IOException {
        Files.createDirectories(root);
    }

    @Override
    public String store(MultipartFile file) {
        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
        String fileName = UUID.randomUUID() + "-" + originalFilename;
        Path destinationFile = root.resolve(fileName).normalize();
        try {
            Files.copy(file.getInputStream(), destinationFile, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new FileStorageException("Falha ao copiar arquivo - ", e);
        }
        return fileName;
    }

    public Resource load(String filename) {
        Path file = root.resolve(filename).normalize();
        Resource resource;

        try {
            resource = new UrlResource(file.toUri());
        } catch (MalformedURLException e) {
            throw new FileNotFoundException("Caminho do arquivo invalido: ", filename, e);
        }

        if (resource.exists() && resource.isReadable()) {
            return resource;
        } else {
            throw new FileNotFoundException("Arquivo não foi encontrado ou não pode ser lido: ", filename);
        }
    }
}

