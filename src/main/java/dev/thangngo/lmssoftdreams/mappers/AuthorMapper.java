package dev.thangngo.lmssoftdreams.mappers;

import dev.thangngo.lmssoftdreams.dtos.request.author.AuthorCreateRequest;
import dev.thangngo.lmssoftdreams.dtos.request.author.AuthorUpdateRequest;
import dev.thangngo.lmssoftdreams.dtos.response.author.AuthorResponse;
import dev.thangngo.lmssoftdreams.entities.Author;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AuthorMapper {

    Author toAuthor(AuthorCreateRequest authorCreateRequest);

    AuthorResponse toAuthorResponse(Author author);

    AuthorUpdateRequest toAuthorUpdateRequest(Author author);

    void updateAuthorFromDto(AuthorUpdateRequest dto, @MappingTarget Author entity);
}
