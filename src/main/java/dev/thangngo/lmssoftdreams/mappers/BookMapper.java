package dev.thangngo.lmssoftdreams.mappers;

import dev.thangngo.lmssoftdreams.dtos.request.book.BookCreateRequest;
import dev.thangngo.lmssoftdreams.dtos.request.book.BookUpdateRequest;
import dev.thangngo.lmssoftdreams.dtos.response.book.BookDetailResponse;
import dev.thangngo.lmssoftdreams.dtos.response.book.BookResponse;
import dev.thangngo.lmssoftdreams.entities.Author;
import dev.thangngo.lmssoftdreams.entities.Book;
import dev.thangngo.lmssoftdreams.entities.Category;
import dev.thangngo.lmssoftdreams.entities.Publisher;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = org.mapstruct.NullValuePropertyMappingStrategy.IGNORE)
public interface BookMapper {

    @Mapping(target = "publisher", source = "publisherId", qualifiedByName = "mapPublisher")
    @Mapping(target = "authors", source = "authorIds", qualifiedByName = "mapAuthors")
    @Mapping(target = "categories", source = "categoryIds", qualifiedByName = "mapCategories")
    Book toBook(BookCreateRequest request);

    @Mapping(target = "publisherId", source = "publisher.id")
    @Mapping(target = "authorIds", source = "authors", qualifiedByName = "mapAuthorIds")
    @Mapping(target = "categoryIds", source = "categories", qualifiedByName = "mapCategoryIds")
    BookResponse toBookResponse(Book book);

    BookDetailResponse toBookDetailResponse(Book book);

    void updateBookFromDto(BookUpdateRequest request, @MappingTarget Book book);


    @Named("mapPublisher")
    default Publisher mapPublisher(Long publisherId) {
        if (publisherId == null) return null;
        Publisher publisher = new Publisher();
        publisher.setId(publisherId);
        return publisher;
    }

    @Named("mapAuthors")
    default Set<Author> mapAuthors(Set<Long> authorIds) {
        if (authorIds == null) return null;
        return authorIds.stream().map(id -> {
            Author a = new Author();
            a.setId(id);
            return a;
        }).collect(Collectors.toSet());
    }

    @Named("mapCategories")
    default Set<Category> mapCategories(Set<Long> categoryIds) {
        if (categoryIds == null) return null;
        return categoryIds.stream().map(id -> {
            Category c = new Category();
            c.setId(id);
            return c;
        }).collect(Collectors.toSet());
    }

    @Named("mapAuthorIds")
    default Set<Long> mapAuthorIds(Set<Author> authors) {
        if (authors == null) return null;
        return authors.stream().map(Author::getId).collect(Collectors.toSet());
    }

    @Named("mapCategoryIds")
    default Set<Long> mapCategoryIds(Set<Category> categories) {
        if (categories == null) return null;
        return categories.stream().map(Category::getId).collect(Collectors.toSet());
    }
}
