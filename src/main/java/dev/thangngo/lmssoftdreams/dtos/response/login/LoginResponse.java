package dev.thangngo.lmssoftdreams.dtos.response.login;

import dev.thangngo.lmssoftdreams.enums.UserRole;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class LoginResponse implements Serializable {
    private UUID userId;
    private String username;
    private String fullName;
    private String token;
    private String refreshToken;
    private UserRole userRole;
}
