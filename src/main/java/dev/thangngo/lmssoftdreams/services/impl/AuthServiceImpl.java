package dev.thangngo.lmssoftdreams.services.impl;

import dev.thangngo.lmssoftdreams.dtos.request.auth.RegisterRequest;
import dev.thangngo.lmssoftdreams.dtos.response.login.LoginResponse;
import dev.thangngo.lmssoftdreams.dtos.response.user.UserResponse;
import dev.thangngo.lmssoftdreams.entities.User;
import dev.thangngo.lmssoftdreams.enums.ErrorCode;
import dev.thangngo.lmssoftdreams.enums.UserRole;
import dev.thangngo.lmssoftdreams.enums.UserStatus;
import dev.thangngo.lmssoftdreams.exceptions.AppException;
import dev.thangngo.lmssoftdreams.mappers.UserMapper;
import dev.thangngo.lmssoftdreams.repositories.UserRepository;
import dev.thangngo.lmssoftdreams.securities.JwtUtil;
import dev.thangngo.lmssoftdreams.services.AuthService;
import dev.thangngo.lmssoftdreams.services.RefreshTokenService; // Thêm import Redis Service
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Date;

@Slf4j
@Service("authService")
@Transactional
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenService refreshTokenService; // Inject RefreshTokenService

    public AuthServiceImpl(UserRepository userRepository, JwtUtil jwtUtil, UserMapper userMapper, PasswordEncoder passwordEncoder, RefreshTokenService refreshTokenService) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.refreshTokenService = refreshTokenService;
    }

    @Override
    public LoginResponse login(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new AppException(ErrorCode.PASS_WORD_NOT_MATCH);
        }

        String accessToken = jwtUtil.generateAccessToken(user.getUsername(), user.getRole().toString());
        String refreshToken = jwtUtil.generateRefreshToken(user.getUsername());
        String jti = jwtUtil.getJtiFromToken(refreshToken);
        Date expiryDate = jwtUtil.getExpirationDateFromToken(refreshToken);
        Duration duration = Duration.between(new Date().toInstant(), expiryDate.toInstant());
        refreshTokenService.saveRefreshToken(jti, duration);

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setUserId(user.getId());
        loginResponse.setUsername(user.getUsername());
        loginResponse.setFullName(user.getFullName());
        loginResponse.setToken(accessToken);
        loginResponse.setUserRole(user.getRole());
        loginResponse.setRefreshToken(refreshToken);

        return loginResponse;
    }


    @Override
    public UserResponse register(RegisterRequest registerRequest) {
        if (userRepository.findByUsername(registerRequest.getUsername()).isPresent()) {
            throw new AppException(ErrorCode.USER_ALREADY_EXISTS);
        }
        if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            throw new AppException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }
        if(userRepository.findByPhoneNumber(registerRequest.getPhoneNumber()).isPresent()){
            throw new AppException(ErrorCode.PHONE_NUMBER_ALREADY_EXISTS);
        }
        User user = userMapper.toUser(registerRequest);
        user.setRole(UserRole.READER);
        user.setStatus(UserStatus.ACTIVE);
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        return userMapper.toUserResponse(userRepository.save(user));
    }

    @Override
    public LoginResponse refreshToken(String refreshToken) {
        String username;
        String oldJti;

        try {
            if (!jwtUtil.validateToken(refreshToken)) {
                throw new AppException(ErrorCode.INVALID_REFRESH_TOKEN);
            }
            username = jwtUtil.getUsernameFromToken(refreshToken);
            oldJti = jwtUtil.getJtiFromToken(refreshToken);
        } catch (ExpiredJwtException ex) {
            throw new AppException(ErrorCode.REFRESH_TOKEN_EXPIRED);
        } catch (Exception ex) {
            throw new AppException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        if (!refreshTokenService.isValidRefreshToken(oldJti)) {
            log.warn("Attempt to use invalid or revoked refresh token: {}", oldJti);
            throw new AppException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        refreshTokenService.revokeRefreshToken(oldJti);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        String newAccessToken = jwtUtil.generateAccessToken(username, user.getRole().toString());
        String newRefreshToken = jwtUtil.generateRefreshToken(username);

        String newJti = jwtUtil.getJtiFromToken(newRefreshToken);
        Date newExpiryDate = jwtUtil.getExpirationDateFromToken(newRefreshToken);
        Duration duration = Duration.between(new Date().toInstant(), newExpiryDate.toInstant());
        refreshTokenService.saveRefreshToken(newJti, duration);

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setUserId(user.getId());
        loginResponse.setUsername(username);
        loginResponse.setFullName(user.getFullName());
        loginResponse.setToken(newAccessToken);
        loginResponse.setRefreshToken(newRefreshToken);

        return loginResponse;
    }
}
