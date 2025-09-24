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
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service("authService")
@Transactional
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;


    public AuthServiceImpl(UserRepository userRepository, JwtUtil jwtUtil, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public LoginResponse login(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new AppException(ErrorCode.PASS_WORD_NOT_MATCH);
        }

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setUserId(user.getId());
        loginResponse.setUsername(user.getUsername());
        loginResponse.setFullName(user.getFullName());
        loginResponse.setToken(jwtUtil.generateAccessToken(user.getUsername()));
        loginResponse.setRefreshToken(jwtUtil.generateRefreshToken(user.getUsername()));

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
    public String refreshToken(String refreshToken) {
        if (!jwtUtil.validateToken(refreshToken)) {
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }
        String username = jwtUtil.getUsernameFromToken(refreshToken);
        return jwtUtil.generateAccessToken(username);
    }

}
