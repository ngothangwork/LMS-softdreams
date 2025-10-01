package dev.thangngo.lmssoftdreams.controllers;

import dev.thangngo.lmssoftdreams.dtos.request.bookcopy.BookCopyCreateRequest;
import dev.thangngo.lmssoftdreams.dtos.request.bookcopy.BookCopyUpdateRequest;
import dev.thangngo.lmssoftdreams.dtos.response.ApiResponse;
import dev.thangngo.lmssoftdreams.dtos.response.bookcopy.BookCopyListResponse;
import dev.thangngo.lmssoftdreams.dtos.response.bookcopy.BookCopyResponse;
import dev.thangngo.lmssoftdreams.services.BookCopyService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/book-copies")
public class BookCopyController {

    private final BookCopyService bookCopyService;

    public BookCopyController(BookCopyService bookCopyService) {
        this.bookCopyService = bookCopyService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<BookCopyResponse>> createBookCopy(
            @Valid @RequestBody BookCopyCreateRequest request) {
        BookCopyResponse response = bookCopyService.createBookCopy(request);
        return ResponseEntity
                .created(URI.create("/book-copies/" + response.getId()))
                .body(ApiResponse.<BookCopyResponse>builder()
                        .message("Book copy created successfully")
                        .code(201)
                        .success(true)
                        .result(response)
                        .build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<BookCopyResponse>> updateBookCopy(
            @PathVariable Long id,
            @Valid @RequestBody BookCopyUpdateRequest request) {
        BookCopyResponse response = bookCopyService.updateBookCopy(id, request);
        return ResponseEntity.ok(ApiResponse.<BookCopyResponse>builder()
                .message("Book copy updated successfully")
                .code(200)
                .success(true)
                .result(response)
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BookCopyResponse>> getBookCopyById(@PathVariable Long id) {
        BookCopyResponse response = bookCopyService.getBookCopyById(id);
        return ResponseEntity.ok(ApiResponse.<BookCopyResponse>builder()
                .message("Book copy fetched successfully")
                .code(200)
                .success(true)
                .result(response)
                .build());
    }

    @GetMapping("/book/{id}")
    public ResponseEntity<ApiResponse<List<BookCopyListResponse>>> getAllBookCopies(@PathVariable Long id) {
        List<BookCopyListResponse> response = bookCopyService.getListBookCopyResponse(id);
        return ResponseEntity.ok(ApiResponse.<List<BookCopyListResponse>>builder()
                .message("Book copies fetched successfully")
                .code(200)
                .success(true)
                .result(response)
                .build());
    }

    @GetMapping("/book/{id}/status")
    public ResponseEntity<ApiResponse<List<BookCopyListResponse>>> getBookCopiesByIdAndStatus (@PathVariable Long id){
        List<BookCopyListResponse> responses = bookCopyService.getListBookCopyResponseByStatus("AVAILABLE", id);
        return ResponseEntity.ok(ApiResponse.<List<BookCopyListResponse>>builder()
                .message("Book copies fetched successfully")
                .code(200)
                .success(true)
                .result(responses)
                .build());
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteBookCopy(@PathVariable Long id) {
        bookCopyService.deleteBookCopy(id);
        return ResponseEntity.ok(ApiResponse.<String>builder()
                .message("Book copy deleted successfully")
                .code(200)
                .success(true)
                .build());
    }



}
