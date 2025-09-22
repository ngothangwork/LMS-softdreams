package dev.thangngo.lmssoftdreams.controllers;

import dev.thangngo.lmssoftdreams.dtos.request.publisher.PublisherCreateRequest;
import dev.thangngo.lmssoftdreams.dtos.request.publisher.PublisherUpdateRequest;
import dev.thangngo.lmssoftdreams.dtos.response.ApiResponse;
import dev.thangngo.lmssoftdreams.dtos.response.publisher.PublisherResponse;
import dev.thangngo.lmssoftdreams.services.PublisherService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/publishers")
public class PublisherController {

    private final PublisherService publisherService;

    public PublisherController(PublisherService publisherService) {
        this.publisherService = publisherService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<PublisherResponse>>> getAllPublishers() {
        List<PublisherResponse> publishers = publisherService.getAllPublishers();
        return ResponseEntity.ok(
                ApiResponse.<List<PublisherResponse>>builder()
                        .message("Get all publishers")
                        .code(200)
                        .success(true)
                        .result(publishers)
                        .build()
        );
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<PublisherResponse>> getPublisherByName(@RequestParam String name) {
        PublisherResponse publisher = publisherService.getPublisherByName(name);
        return ResponseEntity.ok(
                ApiResponse.<PublisherResponse>builder()
                        .message("Get publisher by name: " + name)
                        .code(200)
                        .success(true)
                        .result(publisher)
                        .build()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PublisherResponse>> getPublisherById(@PathVariable Long id) {
        PublisherResponse publisher = publisherService.getPublisherById(id);
        return ResponseEntity.ok(
                ApiResponse.<PublisherResponse>builder()
                        .message("Get publisher by ID: " + id)
                        .code(200)
                        .success(true)
                        .result(publisher)
                        .build()
        );
    }

    @PostMapping
    public ResponseEntity<ApiResponse<PublisherResponse>> createPublisher(@Valid @RequestBody PublisherCreateRequest request) {
        PublisherResponse publisher = publisherService.createPublisher(request);
        return ResponseEntity.status(201).body(
                ApiResponse.<PublisherResponse>builder()
                        .message("Create publisher successfully")
                        .code(201)
                        .success(true)
                        .result(publisher)
                        .build()
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PublisherResponse>> updatePublisher(@Valid @RequestBody PublisherUpdateRequest request,
                                                                          @PathVariable Long id) {
        PublisherResponse publisher = publisherService.updatePublisher(id, request);
        return ResponseEntity.ok(
                ApiResponse.<PublisherResponse>builder()
                        .message("Update publisher successfully")
                        .code(200)
                        .success(true)
                        .result(publisher)
                        .build()
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deletePublisher(@PathVariable Long id) {
        publisherService.deletePublisher(id);
        return ResponseEntity.ok(
                ApiResponse.<String>builder()
                        .message("Delete publisher successfully")
                        .code(200)
                        .success(true)
                        .build()
        );
    }
}
