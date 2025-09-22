package dev.thangngo.lmssoftdreams.controllers;

import dev.thangngo.lmssoftdreams.dtos.request.borrow.BorrowCreateRequest;
import dev.thangngo.lmssoftdreams.dtos.request.borrow.BorrowUpdateRequest;
import dev.thangngo.lmssoftdreams.dtos.response.ApiResponse;
import dev.thangngo.lmssoftdreams.dtos.response.borrow.BorrowResponse;
import dev.thangngo.lmssoftdreams.services.BorrowService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/borrows")
public class BorrowController {

    private final BorrowService borrowService;

    public BorrowController(BorrowService borrowService) {
        this.borrowService = borrowService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<BorrowResponse>> createBorrow(
            @Valid @RequestBody BorrowCreateRequest request) {

        BorrowResponse response = borrowService.createBorrow(request);
        return ResponseEntity.ok(ApiResponse.<BorrowResponse>builder()
                .success(true)
                .code(200)
                .message("Borrow created successfully")
                .result(response)
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BorrowResponse>> getBorrowById(@PathVariable Long id) {
        BorrowResponse response = borrowService.getBorrowById(id);
        return ResponseEntity.ok(ApiResponse.<BorrowResponse>builder()
                .success(true)
                .code(200)
                .message("Borrow fetched successfully")
                .result(response)
                .build());
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<BorrowResponse>>> getAllBorrows() {
        List<BorrowResponse> response = borrowService.getAllBorrows();
        return ResponseEntity.ok(ApiResponse.<List<BorrowResponse>>builder()
                .success(true)
                .code(200)
                .message("Borrows fetched successfully")
                .result(response)
                .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<BorrowResponse>> updateBorrow(
            @PathVariable Long id,
            @Valid @RequestBody BorrowUpdateRequest request) {

        BorrowResponse response = borrowService.updateBorrow(id, request);
        return ResponseEntity.ok(ApiResponse.<BorrowResponse>builder()
                .success(true)
                .code(200)
                .message("Borrow updated successfully")
                .result(response)
                .build());
    }

}
