package dev.thangngo.lmssoftdreams.services.impl;

import dev.thangngo.lmssoftdreams.dtos.request.user.UserCreateRequest;
import dev.thangngo.lmssoftdreams.dtos.request.user.UserUpdateRequest;
import dev.thangngo.lmssoftdreams.dtos.response.user.UserResponse;
import dev.thangngo.lmssoftdreams.entities.User;
import dev.thangngo.lmssoftdreams.enums.UserRole;
import dev.thangngo.lmssoftdreams.enums.UserStatus;
import dev.thangngo.lmssoftdreams.exceptions.AppException;
import dev.thangngo.lmssoftdreams.enums.ErrorCode;
import dev.thangngo.lmssoftdreams.mappers.UserMapper;
import dev.thangngo.lmssoftdreams.repositories.users.UserRepository;
import dev.thangngo.lmssoftdreams.services.UserService;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.UUID;

@Service("userService")
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream().map(userMapper::toUserResponse).toList();
    }

    @Override
    public UserResponse getUserByUsername(String username) {
        User user = userRepository.findByUsername(username).orElse(null);
        if (user != null) {
            return userMapper.toUserResponse(user);
        }else{
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }
    }

    @Override
    public UserResponse createUser(UserCreateRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new AppException(ErrorCode.USER_ALREADY_EXISTS);
        }
        User user = userMapper.toUser(request);
        user.setRole(UserRole.READER);
        user.setStatus(UserStatus.ACTIVE);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        return userMapper.toUserResponse(userRepository.save(user));
    }

    @Override
    public UserResponse getUserById(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        return userMapper.toUserResponse(user);
    }

    @Override
    public UserResponse updateUser(UUID id, UserUpdateRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        if (request.getFullName() != null) {
            user.setFullName(request.getFullName().trim());
        }
        if (request.getPhoneNumber() != null) {
            user.setPhoneNumber(request.getPhoneNumber().trim());
        }
        if (request.getAddress() != null) {
            user.setAddress(request.getAddress().trim());
        }
        if (request.getGender() != null) {
            user.setGender(request.getGender().trim());
        }
        if (request.getEmail() != null) {
            user.setEmail(request.getEmail().trim());
        }
        if (request.getPassword() != null) {
            String encodedPassword = passwordEncoder.encode(request.getPassword());
            user.setPassword(encodedPassword);
        }
        if (request.getDob() != null) {
            try {
                LocalDate dob = LocalDate.parse(request.getDob(), DateTimeFormatter.ISO_DATE);
                user.setDob(dob);
            } catch (DateTimeParseException e) {
                throw new AppException(ErrorCode.INVALID_DATE_FORMAT);
            }
        }

        userRepository.save(user);

        return userMapper.toUserResponse(user);
    }


    @Override
    public void deleteUser(UUID id) {
        User user = userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        userRepository.delete(user);
    }
}
