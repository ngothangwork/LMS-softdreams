package dev.thangngo.lmssoftdreams.mappers;

import dev.thangngo.lmssoftdreams.dtos.request.publisher.PublisherCreateRequest;
import dev.thangngo.lmssoftdreams.dtos.request.publisher.PublisherUpdateRequest;
import dev.thangngo.lmssoftdreams.dtos.response.publisher.PublisherResponse;
import dev.thangngo.lmssoftdreams.entities.Publisher;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = org.mapstruct.NullValuePropertyMappingStrategy.IGNORE)
public interface PublisherMapper {
    PublisherResponse toPublisherResponse(Publisher publisher);

    Publisher toPublisher(PublisherCreateRequest request);

    Publisher toPublisher(PublisherUpdateRequest request);

    void updatePublisherFromDto(PublisherUpdateRequest request, @MappingTarget Publisher publisher);
}
