package com.nimbus.nimbusWebServer.controllers;

import com.nimbus.nimbusWebServer.services.StorageService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/teste")
public class TesteController {

    private final StorageService storageService;

    public TesteController(StorageService storageService) {
        this.storageService = storageService;
    }

    @PostMapping("/save_image")
    public String storeImageTest(@RequestParam("file") MultipartFile file) throws IOException {
        String name = storageService.store(file);
        return name;
    }

    @GetMapping("/arquivo/{filename}")
    public ResponseEntity<Resource> loadImageTest(@PathVariable String filename) {
        Resource resource = storageService.load(filename);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
