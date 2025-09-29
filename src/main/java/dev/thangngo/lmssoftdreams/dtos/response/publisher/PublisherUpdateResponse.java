package dev.thangngo.lmssoftdreams.dtos.response.publisher;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PublisherUpdateResponse {
    private Long id;
    private String name;
}
