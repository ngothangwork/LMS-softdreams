package dev.thangngo.lmssoftdreams.dtos.response.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserResponse {
    private String fullName;
    private String gender;
    private String dob;
    private String avatar;
    private String phoneNumber;
    private String address;
    private String email;
    private String username;
}
