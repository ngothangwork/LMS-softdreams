package dev.thangngo.lmssoftdreams.services;

import dev.thangngo.lmssoftdreams.dtos.request.author.AuthorCreateRequest;
import dev.thangngo.lmssoftdreams.dtos.request.author.AuthorUpdateRequest;
import dev.thangngo.lmssoftdreams.dtos.response.author.AuthorResponse;

import java.util.List;

public interface AuthorService {
    AuthorResponse getAuthorById(Long id);
    List<AuthorResponse> getAuthorByName(String username);
    List<AuthorResponse> getAllAuthors();
    AuthorResponse createAuthor(AuthorCreateRequest authorCreateRequest);
    AuthorResponse updateAuthor(Long id, AuthorUpdateRequest authorUpdateRequest);
    void deleteAuthor(Long id);

}
