package dev.thangngo.lmssoftdreams.dtos.response.author;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AuthorUpdateResponse {
    private Long id;
    private String name;

    public AuthorUpdateResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
