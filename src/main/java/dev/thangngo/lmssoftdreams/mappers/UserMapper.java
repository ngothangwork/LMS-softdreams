package dev.thangngo.lmssoftdreams.mappers;

import dev.thangngo.lmssoftdreams.dtos.request.auth.RegisterRequest;
import dev.thangngo.lmssoftdreams.dtos.request.user.UserCreateRequest;
import dev.thangngo.lmssoftdreams.dtos.response.user.UserResponse;
import dev.thangngo.lmssoftdreams.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {
    User toUser(UserCreateRequest userCreateRequest);

    User toUser(RegisterRequest registerRequest);

    UserResponse toUserResponse(User user);
}
