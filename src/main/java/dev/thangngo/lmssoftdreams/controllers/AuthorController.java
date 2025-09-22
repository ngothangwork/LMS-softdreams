package dev.thangngo.lmssoftdreams.controllers;

import dev.thangngo.lmssoftdreams.dtos.request.author.AuthorCreateRequest;
import dev.thangngo.lmssoftdreams.dtos.request.author.AuthorUpdateRequest;
import dev.thangngo.lmssoftdreams.dtos.response.ApiResponse;
import dev.thangngo.lmssoftdreams.dtos.response.author.AuthorResponse;
import dev.thangngo.lmssoftdreams.services.AuthorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/authors")
public class AuthorController {

    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @PostMapping()
    public ResponseEntity<ApiResponse<AuthorResponse>> createAuthor(@Valid @RequestBody AuthorCreateRequest authorCreateRequest) {
        AuthorResponse authorResponse = authorService.createAuthor(authorCreateRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.<AuthorResponse>builder()
                .message("Create Author Successfully")
                .code(201)
                .success(true)
                .result(authorResponse)
                .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AuthorResponse>> updateAuthor(@Valid @RequestBody AuthorUpdateRequest authorUpdateRequest, @PathVariable Long id) {
        AuthorResponse authorResponse = authorService.updateAuthor(id, authorUpdateRequest);
        return ResponseEntity.ok(ApiResponse.<AuthorResponse>builder()
                .message("Update Author Successfully")
                .code(200)
                .success(true)
                .result(authorResponse)
                .build());
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<ApiResponse<List<AuthorResponse>>> getAuthorByName(@PathVariable String name) {
        List<AuthorResponse> authorResponseList = authorService.getAuthorByName(name);
        return ResponseEntity.ok(
                ApiResponse.<List<AuthorResponse>>builder()
                        .message("Get list author contain string " + name)
                        .code(200)
                        .success(true)
                        .result(authorResponseList)
                        .build()
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteAuthor(@PathVariable Long id) {
        authorService.deleteAuthor(id);
        return ResponseEntity.ok(ApiResponse.<String>builder()
                .message("Delete Author Successfully")
                .code(200)
                .success(true)
                .build());
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<AuthorResponse>>> getAllAuthor() {
        List<AuthorResponse> authorResponseList = authorService.getAllAuthors();
        return ResponseEntity.ok(ApiResponse.<List<AuthorResponse>>builder()
                .message("Get all authors")
                .result(authorResponseList)
                .code(200)
                .success(true)
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AuthorResponse>> getAuthorById(@PathVariable Long id) {
        AuthorResponse authorResponseList = authorService.getAuthorById(id);
        return ResponseEntity.ok(ApiResponse.<AuthorResponse>builder()
                .message("Get all authors")
                .result(authorResponseList)
                .code(200)
                .success(true)
                .build());
    }
}
