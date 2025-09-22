package dev.thangngo.lmssoftdreams.dtos.response.publisher;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PublisherResponse {
    private Long id;
    private String name;
    private String address;
    private String phone;
}
