package dev.thangngo.lmssoftdreams.services.impl;

import dev.thangngo.lmssoftdreams.dtos.request.book.BookCreateRequest;
import dev.thangngo.lmssoftdreams.dtos.request.book.BookSearchRequest;
import dev.thangngo.lmssoftdreams.dtos.request.book.BookUpdateRequest;
import dev.thangngo.lmssoftdreams.dtos.response.PageResponse;
import dev.thangngo.lmssoftdreams.dtos.response.book.BookDetailResponse;
import dev.thangngo.lmssoftdreams.dtos.response.book.BookDetailResponseDTO;
import dev.thangngo.lmssoftdreams.dtos.response.book.BookResponse;
import dev.thangngo.lmssoftdreams.entities.*;
import dev.thangngo.lmssoftdreams.enums.BookStatus;
import dev.thangngo.lmssoftdreams.enums.ErrorCode;
import dev.thangngo.lmssoftdreams.exceptions.AppException;
import dev.thangngo.lmssoftdreams.mappers.BookMapper;
import dev.thangngo.lmssoftdreams.repositories.AuthorRepository;
import dev.thangngo.lmssoftdreams.repositories.BookCopyRepository;
import dev.thangngo.lmssoftdreams.repositories.BookRepository;
import dev.thangngo.lmssoftdreams.repositories.CategoryRepository;
import dev.thangngo.lmssoftdreams.repositories.PublisherRepository;
import dev.thangngo.lmssoftdreams.services.BookService;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;

import java.util.Collections;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final AuthorRepository authorRepository;
    private final CategoryRepository categoryRepository;
    private final PublisherRepository publisherRepository;
    private final BookCopyRepository bookCopyRepository;

    public BookServiceImpl(BookRepository bookRepository,
                           BookMapper bookMapper,
                           AuthorRepository authorRepository,
                           CategoryRepository categoryRepository,
                           PublisherRepository publisherRepository,
                           BookCopyRepository bookCopyRepository) {
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
        this.authorRepository = authorRepository;
        this.categoryRepository = categoryRepository;
        this.publisherRepository = publisherRepository;
        this.bookCopyRepository = bookCopyRepository;
    }

    @Override
    @CacheEvict(value = "books", allEntries = true)
    public BookResponse createBook(BookCreateRequest request) {
        Set<Author> authors = loadAuthorsOrThrow(request.getAuthorIds());
        Set<Category> categories = loadCategoriesOrThrow(request.getCategoryIds());
        Publisher publisher = loadPublisherOrThrow(request.getPublisherId());

        if (bookRepository.existsByIsbn(request.getIsbn())) {
            throw new AppException(ErrorCode.ISBN_ALREADY_EXISTS);
        }

        Book book = bookMapper.toBook(request);
        book.setAuthors(authors);
        book.setCategories(categories);
        book.setPublisher(publisher);

        return bookMapper.toBookResponse(bookRepository.save(book));
    }


    @Override
    @CacheEvict(value = "books-filter", allEntries = true)
    public BookResponse updateBook(Long id, BookUpdateRequest request) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.BOOK_NOT_FOUND));
        if (StringUtils.hasText(request.getIsbn()) && !request.getIsbn().equals(book.getIsbn()) && bookRepository.existsByIsbnAndIdNot(request.getIsbn(), id)) {
            throw new AppException(ErrorCode.ISBN_ALREADY_EXISTS);
        }

        bookMapper.updateBookFromDto(request, book);

        if (request.getAuthorIds() != null) {
            Set<Author> authors = loadAuthorsOrThrow(request.getAuthorIds());
            book.setAuthors(authors);
        }

        if (request.getCategoryIds() != null) {
            Set<Category> categories = loadCategoriesOrThrow(request.getCategoryIds());
            book.setCategories(categories);
        }

        if (request.getPublisherId() != null) {
            Publisher publisher = loadPublisherOrThrow(request.getPublisherId());
            book.setPublisher(publisher);
        }

        Book saved = bookRepository.save(book);
        return bookMapper.toBookResponse(saved);
    }

    @Override
    @CacheEvict(value = "books-filter", allEntries = true)
    public void deleteBook(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.BOOK_NOT_FOUND));
        bookRepository.delete(book);
    }

    @Override
    public BookDetailResponse getBookById(Long id) {
        BookDetailResponse bookDetailResponse = bookRepository.findById(id)
                .map(bookMapper::toBookDetailResponse)
                .orElseThrow(() -> new AppException(ErrorCode.BOOK_NOT_FOUND));
        int numberOfBookBorrow = bookCopyRepository.countByStatusAndBookId(BookStatus.UNAVAILABLE, id);
        int numberOfBookAvailable = bookCopyRepository.countByStatusAndBookId(BookStatus.AVAILABLE, id);
        bookDetailResponse.setNumberOfAvailable(numberOfBookAvailable);
        bookDetailResponse.setNumberOfBorrowed(numberOfBookBorrow);
        return bookDetailResponse;
    }

    @Override
    public List<BookResponse> getAllBooks() {
        return bookRepository.findAll()
                .stream()
                .map(bookMapper::toBookResponse)
                .toList();
    }

    @Override
    @Cacheable(value = "books", key = "#name")
    public List<BookResponse> getBooksByName(String name) {
        return bookRepository.findAll()
                .stream()
                .filter(b -> b.getName().toLowerCase().contains(name.toLowerCase()))
                .map(bookMapper::toBookResponse)
                .toList();
    }

    @Override
    public BookUpdateRequest getBookUpdateById(Long id) {
        return bookRepository.findById(id)
                .map(bookMapper::toBookUpdateRequest)
                .orElseThrow(() -> new AppException(ErrorCode.BOOK_NOT_FOUND));
    }

    @Override
//    @Cacheable(
//            value = "books-filter",
//            key = "{#request.type, #request.keyword, #pageable.pageNumber, #pageable.pageSize}"
//    )
    public PageResponse<BookResponse> filterBooks(BookSearchRequest request, Pageable pageable) {
        List<BookDetailResponseDTO> dtoList = bookRepository.filterBooks(request.getType(), request.getKeyword(), pageable);
        long total = bookRepository.countFilterBooks(request.getType(), request.getKeyword());
        List<BookResponse> responses = dtoList.stream()
                .map(dto -> {
                    BookResponse res = new BookResponse();
                    res.setId(dto.getId());
                    res.setName(dto.getName());
                    res.setAvatar(dto.getAvatar());
                    res.setIsbn(dto.getIsbn());
                    return res;
                })
                .toList();
        Page<BookResponse> page = new PageImpl<>(responses, pageable, total);
        return new PageResponse<>(page);
    }


    @Override
    public List<BookDetailResponseDTO> searchBooks(String name, Pageable pageable) {
        return bookRepository.searchBooksByName(name, pageable);
    }

    private Set<Author> loadAuthorsOrThrow(Set<Long> authorIds) {
        if (authorIds == null || authorIds.isEmpty()) return Collections.emptySet();
        List<Author> authors = authorRepository.findAllById(authorIds);
        Set<Long> foundIds = authors.stream().map(Author::getId).collect(Collectors.toSet());
        Set<Long> notFound = new HashSet<>(authorIds);
        notFound.removeAll(foundIds);
        if (!notFound.isEmpty()) throw new AppException(ErrorCode.AUTHOR_NOT_FOUND);
        return new HashSet<>(authors);
    }

    private Set<Category> loadCategoriesOrThrow(Set<Long> categoryIds) {
        if (categoryIds == null || categoryIds.isEmpty()) return Collections.emptySet();
        List<Category> categories = categoryRepository.findAllById(categoryIds);
        Set<Long> foundIds = categories.stream().map(Category::getId).collect(Collectors.toSet());
        Set<Long> notFound = new HashSet<>(categoryIds);
        notFound.removeAll(foundIds);
        if (!notFound.isEmpty()) throw new AppException(ErrorCode.CATEGORY_NOT_FOUND);
        return new HashSet<>(categories);
    }

    private Publisher loadPublisherOrThrow(Long publisherId) {
        if (publisherId == null) return null;
        return publisherRepository.findById(publisherId)
                .orElseThrow(() -> new AppException(ErrorCode.PUBLISHER_NOT_FOUND));
    }
}
