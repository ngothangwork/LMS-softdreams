package dev.thangngo.lmssoftdreams.services;

import dev.thangngo.lmssoftdreams.dtos.request.auth.RegisterRequest;
import dev.thangngo.lmssoftdreams.dtos.response.login.LoginResponse;
import dev.thangngo.lmssoftdreams.dtos.response.user.UserResponse;

public interface AuthService {
    LoginResponse login(String username, String password);
    UserResponse register(RegisterRequest registerRequest);
    LoginResponse refreshToken(String refreshToken);
}
