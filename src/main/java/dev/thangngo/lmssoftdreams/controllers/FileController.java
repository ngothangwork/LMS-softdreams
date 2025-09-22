package dev.thangngo.lmssoftdreams.controllers;

import dev.thangngo.lmssoftdreams.dtos.response.ApiResponse;
import dev.thangngo.lmssoftdreams.services.FileStorageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/files")
public class FileController {

    private final FileStorageService fileStorageService;

    public FileController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @PostMapping("/upload")
    public ResponseEntity<ApiResponse<String>> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        String fileUrl = fileStorageService.saveFile(file);
        return ResponseEntity.ok(
                ApiResponse.<String>builder()
                        .success(true)
                        .code(200)
                        .message("File uploaded successfully")
                        .result(fileUrl)
                        .build()
        );
    }
}


