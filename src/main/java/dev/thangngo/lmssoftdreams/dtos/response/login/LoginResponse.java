package dev.thangngo.lmssoftdreams.dtos.response.login;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class LoginResponse implements Serializable {
    private String username;
    private String fullName;
    private String token;
}
