package dev.thangngo.lmssoftdreams.mappers;

import dev.thangngo.lmssoftdreams.dtos.request.bookcopy.BookCopyCreateRequest;
import dev.thangngo.lmssoftdreams.dtos.request.bookcopy.BookCopyUpdateRequest;
import dev.thangngo.lmssoftdreams.dtos.response.bookcopy.BookCopyResponse;
import dev.thangngo.lmssoftdreams.entities.BookCopy;
import org.mapstruct.*;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface BookCopyMapper {
    BookCopy toBookCopy(BookCopyCreateRequest request);

    BookCopyResponse toBookCopyResponse(BookCopy bookCopy);

    void updateBookCopyFromDto(BookCopyUpdateRequest request, @MappingTarget BookCopy bookCopy);
}
