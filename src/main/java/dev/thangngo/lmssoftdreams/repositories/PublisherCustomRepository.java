package dev.thangngo.lmssoftdreams.repositories;

import dev.thangngo.lmssoftdreams.dtos.response.publisher.PublisherUpdateResponse;

import java.util.List;

public interface PublisherCustomRepository {
    List<PublisherUpdateResponse> findAllPublisherUpdateResponses();
}
