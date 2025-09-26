package dev.thangngo.lmssoftdreams.services.impl;

import dev.thangngo.lmssoftdreams.dtos.request.author.AuthorCreateRequest;
import dev.thangngo.lmssoftdreams.dtos.request.author.AuthorUpdateRequest;
import dev.thangngo.lmssoftdreams.dtos.response.author.AuthorResponse;
import dev.thangngo.lmssoftdreams.entities.Author;
import dev.thangngo.lmssoftdreams.enums.ErrorCode;
import dev.thangngo.lmssoftdreams.exceptions.AppException;
import dev.thangngo.lmssoftdreams.mappers.AuthorMapper;
import dev.thangngo.lmssoftdreams.repositories.AuthorRepository;
import dev.thangngo.lmssoftdreams.services.AuthorService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Pageable;
import java.util.List;

@Service
@Transactional
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;
    private final AuthorMapper authorMapper;

    public AuthorServiceImpl(AuthorRepository authorRepository,
                             AuthorMapper authorMapper) {
        this.authorRepository = authorRepository;
        this.authorMapper = authorMapper;
    }

    @Override
    public AuthorResponse getAuthorById(Long id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.AUTHOR_NOT_FOUND));
        return authorMapper.toAuthorResponse(author);
    }

    @Override
    public Page<AuthorResponse> getAuthorByName(String username, Pageable pageable) {
        Page<Author> page = authorRepository.findByNameContainingIgnoreCase(username, pageable);
        return page.map(authorMapper::toAuthorResponse);
    }



    @Override
    public List<AuthorResponse> getAllAuthors() {
        return authorRepository.findAll()
                .stream()
                .map(authorMapper::toAuthorResponse)
                .toList();
    }

    @Override
    public AuthorResponse createAuthor(AuthorCreateRequest authorCreateRequest) {
        Author author = authorMapper.toAuthor(authorCreateRequest);

        return authorMapper.toAuthorResponse(authorRepository.save(author));
    }

    @Override
    public AuthorResponse updateAuthor(Long id, AuthorUpdateRequest authorUpdateRequest) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.AUTHOR_NOT_FOUND));

        authorMapper.updateAuthorFromDto(authorUpdateRequest, author);

        return authorMapper.toAuthorResponse(authorRepository.save(author));
    }


    @Override
    public void deleteAuthor(Long id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.AUTHOR_NOT_FOUND));

        authorRepository.delete(author);
    }
}
