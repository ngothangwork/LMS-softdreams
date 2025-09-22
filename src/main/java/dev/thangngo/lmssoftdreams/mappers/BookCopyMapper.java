package dev.thangngo.lmssoftdreams.mappers;

import dev.thangngo.lmssoftdreams.dtos.request.bookcopy.BookCopyCreateRequest;
import dev.thangngo.lmssoftdreams.dtos.request.bookcopy.BookCopyUpdateRequest;
import dev.thangngo.lmssoftdreams.dtos.response.bookcopy.BookCopyResponse;
import dev.thangngo.lmssoftdreams.entities.Book;
import dev.thangngo.lmssoftdreams.entities.BookCopy;
import org.mapstruct.*;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface BookCopyMapper {

    @Mapping(target = "book", source = "bookId", qualifiedByName = "mapBook")
    BookCopy toBookCopy(BookCopyCreateRequest request);

    @Mapping(target = "bookId", source = "book.id")
    BookCopyResponse toBookCopyResponse(BookCopy bookCopy);

    void updateBookCopyFromDto(BookCopyUpdateRequest request, @MappingTarget BookCopy bookCopy);

    @Named("mapBook")
    default Book mapBook(Long id) {
        if (id == null) return null;
        Book book = new Book();
        book.setId(id);
        return book;
    }
}
