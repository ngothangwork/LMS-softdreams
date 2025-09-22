package dev.thangngo.lmssoftdreams.dtos.request.author;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthorUpdateRequest {
    @NotBlank(message = "Username is required")
    @Size(min = 8, max = 50, message = "Username must be between 4 and 50 characters")
    private String name;

    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "DOB must be in format yyyy-MM-dd")
    private String dob;

    private String nationality;
    private String description;
}
