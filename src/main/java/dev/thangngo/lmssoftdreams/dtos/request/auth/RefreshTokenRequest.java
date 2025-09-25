package dev.thangngo.lmssoftdreams.dtos.request.auth;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
public class RefreshTokenRequest {
    @NonNull
    private String refreshToken;
}
