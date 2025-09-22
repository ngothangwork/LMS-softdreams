package dev.thangngo.lmssoftdreams.dtos.request.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCreateRequest {

    @NotBlank(message = "Username is required")
    @Size(min = 8, max = 50, message = "Username must be between 4 and 50 characters")
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 100, message = "Password must be at least 6 characters")
    private String password;

    @Email(message = "Invalid email format")
    private String email;

    @Pattern(regexp = "^[0-9]{9,15}$", message = "Phone number must be 9-15 digits")
    private String phoneNumber;

    @Size(max = 255, message = "Address must be less than 255 characters")
    private String address;

    @NotBlank(message = "Full name is required")
    private String fullName;

    private String gender;

    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "DOB must be in format yyyy-MM-dd")
    private String dob;
}
