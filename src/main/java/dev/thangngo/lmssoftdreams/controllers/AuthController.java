package dev.thangngo.lmssoftdreams.controllers;

import dev.thangngo.lmssoftdreams.dtos.request.auth.LoginRequest;
import dev.thangngo.lmssoftdreams.dtos.request.auth.RefreshTokenRequest;
import dev.thangngo.lmssoftdreams.dtos.request.auth.RegisterRequest;
import dev.thangngo.lmssoftdreams.dtos.response.ApiResponse;
import dev.thangngo.lmssoftdreams.dtos.response.login.LoginResponse;
import dev.thangngo.lmssoftdreams.dtos.response.user.UserResponse;
import dev.thangngo.lmssoftdreams.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserResponse>> register(@Valid @RequestBody RegisterRequest registerRequest) {
        UserResponse userResponse = authService.register(registerRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<UserResponse>builder()
                        .message("Register Successfully")
                        .code(201)
                        .success(true)
                        .result(userResponse)
                        .build());
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest loginRequest) {
        LoginResponse loginResponse = authService.login(loginRequest.getUsername(), loginRequest.getPassword());
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(ApiResponse.<LoginResponse>builder()
                        .message("Login Successfully")
                        .code(202)
                        .success(true)
                        .result(loginResponse)
                        .build());
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<String>> refresh(@RequestBody RefreshTokenRequest request) {
        String newAccessToken = authService.refreshToken(request.getRefreshToken());
        return ResponseEntity.ok(
                ApiResponse.<String>builder()
                        .success(true)
                        .code(200)
                        .message("Token refreshed")
                        .result(newAccessToken)
                        .build()
        );
    }




}
