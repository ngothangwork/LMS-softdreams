package dev.thangngo.lmssoftdreams.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileStorageService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    public String saveFile(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }
        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path path = Paths.get(uploadDir, fileName);

        Files.copy(file.getInputStream(), path);

        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/uploads/")
                .path(fileName)
                .toUriString();

    }
}
