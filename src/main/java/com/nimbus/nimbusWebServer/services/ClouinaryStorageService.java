package com.nimbus.nimbusWebServer.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@Primary
public class ClouinaryStorageService implements StorageService{

    @Autowired
    private Cloudinary cloudinary;

    @Override
    public String store(MultipartFile file) throws IOException {
        return uploeadImageInCloudinary(file, "nimbus/produtos");
    }

    @Override
    public Resource load(String filename) {
        return null;
    }

    @Override
    public String getFullUrlImage(String url) {
        return "";
    }

    private String uploeadImageInCloudinary(MultipartFile file, String pastaDeDestino) {
        try {
            Map params = ObjectUtils.asMap(
                    "folder", pastaDeDestino,
                    "resource_type","image"
            );

            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), params);

            return uploadResult.get("secure_url").toString();
        } catch (IOException e) {
            throw new RuntimeException("Erro ao fazer upload da imagem", e);
        }
    }
}
