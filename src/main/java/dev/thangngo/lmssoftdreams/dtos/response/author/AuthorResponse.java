package dev.thangngo.lmssoftdreams.dtos.response.author;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AuthorResponse {
    private Long id;
    private String name;
    private String dob;
    private String nationality;
    private String description;
}
