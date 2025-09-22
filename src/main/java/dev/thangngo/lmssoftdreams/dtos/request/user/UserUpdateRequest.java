package dev.thangngo.lmssoftdreams.dtos.request.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateRequest {

    @Size(max = 100, message = "Full name must be less than 100 characters")
    private String fullName;

    private String gender;

    // ISO-8601 format: yyyy-MM-dd
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$",
            message = "DOB must be in format yyyy-MM-dd (e.g., 1990-12-31)")
    private String dob;

    @Size(min = 6, max = 100, message = "Password must be at least 6 characters")
    private String password;

    @Size(max = 255, message = "Address must be less than 255 characters")
    private String address;

    @Pattern(regexp = "^[0-9]{9,15}$",
            message = "Phone number must be 9-15 digits")
    private String phoneNumber;

    @Email(message = "Invalid email format")
    private String email;
}
