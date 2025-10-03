package dev.thangngo.lmssoftdreams.dtos.request.publisher;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PublisherUpdateRequest {

    @Pattern(regexp = "^[\\p{L} ]+$", message = "Publisher name can only contain letters and spaces")
    @Size(min = 4, max = 100, message = "Publisher name must be between 4 and 100 characters")
    private String name;

    @Size(max = 255, message = "Address must be less than 255 characters")
    private String address;

    @Size(min = 10, max = 15, message = "Phone number must be between 10 and 15 characters")
    private String phone;
}
