package dev.thangngo.lmssoftdreams.services;

import dev.thangngo.lmssoftdreams.dtos.request.user.UserCreateRequest;
import dev.thangngo.lmssoftdreams.dtos.request.user.UserUpdateRequest;
import dev.thangngo.lmssoftdreams.dtos.response.user.UserResponse;


import java.util.List;
import java.util.UUID;

public interface UserService {
    List<UserResponse> getAllUsers();
    UserResponse getUserById(UUID id);
    UserResponse getUserByUsername(String username);
    UserResponse createUser(UserCreateRequest userCreateRequest);
    UserResponse updateUser(UUID id, UserUpdateRequest userUpdateRequest);
    void deleteUser(UUID id);
}
