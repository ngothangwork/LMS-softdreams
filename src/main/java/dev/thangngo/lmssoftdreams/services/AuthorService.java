package dev.thangngo.lmssoftdreams.services;

import dev.thangngo.lmssoftdreams.dtos.request.author.AuthorCreateRequest;
import dev.thangngo.lmssoftdreams.dtos.request.author.AuthorUpdateRequest;
import dev.thangngo.lmssoftdreams.dtos.response.author.AuthorResponse;

import dev.thangngo.lmssoftdreams.dtos.response.author.AuthorUpdateResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface AuthorService {
    AuthorResponse getAuthorById(Long id);
    Page<AuthorResponse> getAuthorByName(String username, Pageable pageable);
    List<AuthorResponse> getAllAuthors();
    AuthorResponse createAuthor(AuthorCreateRequest authorCreateRequest);
    AuthorResponse updateAuthor(Long id, AuthorUpdateRequest authorUpdateRequest);
    void deleteAuthor(Long id);
    List<AuthorUpdateResponse> getAuthorUpdate();

}
