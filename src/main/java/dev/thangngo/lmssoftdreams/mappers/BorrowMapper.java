package dev.thangngo.lmssoftdreams.mappers;

import dev.thangngo.lmssoftdreams.dtos.request.borrow.BorrowCreateRequest;
import dev.thangngo.lmssoftdreams.dtos.request.borrow.BorrowUpdateRequest;
import dev.thangngo.lmssoftdreams.dtos.response.borrow.BorrowResponse;
import dev.thangngo.lmssoftdreams.entities.BookCopy;
import dev.thangngo.lmssoftdreams.entities.Borrow;
import dev.thangngo.lmssoftdreams.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.util.UUID;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = org.mapstruct.NullValuePropertyMappingStrategy.IGNORE)
public interface BorrowMapper {

    @Mapping(target = "bookCopyId", source = "bookCopy.id")
    @Mapping(target = "userId", source = "user.id")
    BorrowResponse toBorrowResponse(Borrow borrow);

    @Mapping(target = "bookCopy", source = "bookCopyId", qualifiedByName = "mapBookCopy")
    @Mapping(target = "user", source = "userId", qualifiedByName = "mapUser")
    Borrow toBorrow(BorrowCreateRequest request);

    @Mapping(target = "bookCopy", source = "bookCopyId", qualifiedByName = "mapBookCopy")
    @Mapping(target = "user", source = "userId", qualifiedByName = "mapUser")
    void updateBorrowFromDto(BorrowUpdateRequest request, @MappingTarget Borrow borrow);

    @Named("mapBookCopy")
    default BookCopy mapBookCopy(Long id) {
        if (id == null) return null;
        BookCopy bookCopy = new BookCopy();
        bookCopy.setId(id);
        return bookCopy;
    }

    @Named("mapUser")
    default User mapUser(UUID id) {
        if (id == null) return null;
        User user = new User();
        user.setId(id);
        return user;
    }
}

