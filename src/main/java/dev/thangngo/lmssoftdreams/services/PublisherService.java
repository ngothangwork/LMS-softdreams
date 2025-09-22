package dev.thangngo.lmssoftdreams.services;

import dev.thangngo.lmssoftdreams.dtos.request.publisher.PublisherCreateRequest;
import dev.thangngo.lmssoftdreams.dtos.request.publisher.PublisherUpdateRequest;
import dev.thangngo.lmssoftdreams.dtos.response.publisher.PublisherResponse;

import java.util.List;

public interface PublisherService {
    PublisherResponse getPublisherById(Long id);
    PublisherResponse getPublisherByName(String name);
    PublisherResponse createPublisher(PublisherCreateRequest request);
    void deletePublisher(Long id);
    PublisherResponse updatePublisher(Long id, PublisherUpdateRequest request);
    List<PublisherResponse> getAllPublishers();
}
