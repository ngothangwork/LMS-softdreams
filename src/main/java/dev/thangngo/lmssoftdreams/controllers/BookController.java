package dev.thangngo.lmssoftdreams.controllers;

import dev.thangngo.lmssoftdreams.dtos.request.book.BookCreateRequest;
import dev.thangngo.lmssoftdreams.dtos.request.book.BookSearchRequest;
import dev.thangngo.lmssoftdreams.dtos.request.book.BookUpdateRequest;
import dev.thangngo.lmssoftdreams.dtos.response.ApiResponse;
import dev.thangngo.lmssoftdreams.dtos.response.book.BookResponse;
import dev.thangngo.lmssoftdreams.services.BookService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<BookResponse>> createBook(
            @Valid @RequestBody BookCreateRequest request) {
        BookResponse response = bookService.createBook(request);
        return ResponseEntity
                .created(URI.create("/books/" + response.getId()))
                .body(ApiResponse.<BookResponse>builder()
                        .message("Book created successfully")
                        .code(201)
                        .success(true)
                        .result(response)
                        .build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<BookResponse>> updateBook(
            @PathVariable Long id,
            @Valid @RequestBody BookUpdateRequest request) {
        BookResponse response = bookService.updateBook(id, request);
        return ResponseEntity.ok(ApiResponse.<BookResponse>builder()
                .message("Book updated successfully")
                .code(200)
                .success(true)
                .result(response)
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BookResponse>> getBookById(@PathVariable Long id) {
        BookResponse response = bookService.getBookById(id);
        return ResponseEntity.ok(ApiResponse.<BookResponse>builder()
                .message("Book fetched successfully")
                .code(200)
                .success(true)
                .result(response)
                .build());
    }

    @GetMapping("")
    public ResponseEntity<ApiResponse<List<BookResponse>>> getBooks(
            @RequestParam(required = false) String name) {
        List<BookResponse> response = (name != null)
                ? bookService.getBooksByName(name)
                : bookService.getAllBooks();

        return ResponseEntity.ok(ApiResponse.<List<BookResponse>>builder()
                .message("Books fetched successfully")
                .code(200)
                .success(true)
                .result(response)
                .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.ok(ApiResponse.<String>builder()
                .message("Book deleted successfully")
                .code(200)
                .success(true)
                .build());
    }

    @PostMapping("/search")
    public ResponseEntity<ApiResponse<Page<BookResponse>>> searchBooks(
            @RequestBody @Valid BookSearchRequest request,
            @PageableDefault(size = 10, sort = "name", direction = Sort.Direction.ASC) Pageable pageable
    ) {
        Page<BookResponse> result = bookService.filterBooks(request, pageable);
        return ResponseEntity.ok(
                ApiResponse.<Page<BookResponse>>builder()
                        .success(true)
                        .code(200)
                        .message("Search books successfully")
                        .result(result)
                        .build()
        );
    }

}
