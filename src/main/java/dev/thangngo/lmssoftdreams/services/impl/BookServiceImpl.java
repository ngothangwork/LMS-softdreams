package dev.thangngo.lmssoftdreams.services.impl;

import dev.thangngo.lmssoftdreams.dtos.request.book.BookCreateRequest;
import dev.thangngo.lmssoftdreams.dtos.request.book.BookSearchRequest;
import dev.thangngo.lmssoftdreams.dtos.request.book.BookUpdateRequest;
import dev.thangngo.lmssoftdreams.dtos.response.book.BookDetailResponse;
import dev.thangngo.lmssoftdreams.dtos.response.book.BookResponse;
import dev.thangngo.lmssoftdreams.entities.*;
import dev.thangngo.lmssoftdreams.enums.BookStatus;
import dev.thangngo.lmssoftdreams.enums.ErrorCode;
import dev.thangngo.lmssoftdreams.exceptions.AppException;
import dev.thangngo.lmssoftdreams.mappers.BookMapper;
import dev.thangngo.lmssoftdreams.repositories.authors.AuthorRepository;
import dev.thangngo.lmssoftdreams.repositories.bookcopy.BookCopyRepository;
import dev.thangngo.lmssoftdreams.repositories.books.BookRepository;
import dev.thangngo.lmssoftdreams.repositories.categories.CategoryRepository;
import dev.thangngo.lmssoftdreams.repositories.publishers.PublisherRepository;
import dev.thangngo.lmssoftdreams.services.BookService;

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
    public BookResponse createBook(BookCreateRequest request) {
        Set<Author> authors = loadAuthorsOrThrow(request.getAuthorIds());
        Set<Category> categories = loadCategoriesOrThrow(request.getCategoryIds());
        Publisher publisher = loadPublisherOrThrow(request.getPublisherId());

        if(bookRepository.existsByIsbn(request.getIsbn())) {
            throw new AppException(ErrorCode.ISBN_ALREADY_EXISTS);
        }

        Book book = bookMapper.toBook(request);
        book.setAuthors(authors);
        book.setCategories(categories);
        book.setPublisher(publisher);

        return bookMapper.toBookResponse(bookRepository.save(book));
    }


    @Override
    public BookResponse updateBook(Long id, BookUpdateRequest request) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.BOOK_NOT_FOUND));
        if (StringUtils.hasText(request.getIsbn()) && !request.getIsbn().equals(book.getIsbn())) {
            if (bookRepository.existsByIsbnAndIdNot(request.getIsbn(), id)) {
                throw new AppException(ErrorCode.ISBN_ALREADY_EXISTS);
            }
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
    public void deleteBook(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.BOOK_NOT_FOUND));
        bookRepository.delete(book);
    }

    @Override
    public BookDetailResponse getBookById(Long id) {

        BookDetailResponse bookDetailResponse =  bookRepository.findById(id)
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
    public List<BookResponse> getBooksByName(String name) {
        return bookRepository.findAll()
                .stream()
                .filter(b -> b.getName().toLowerCase().contains(name.toLowerCase()))
                .map(bookMapper::toBookResponse)
                .toList();
    }

    @Override
    public BookResponse getBookUpdateById(Long id) {
        return bookRepository.findById(id)
                .map(bookMapper::toBookResponse)
                .orElseThrow(() -> new AppException(ErrorCode.BOOK_NOT_FOUND));
    }

    @Override
    public Page<BookResponse> filterBooks(BookSearchRequest request, Pageable pageable) {
        List<Book> books;
        long total;

        switch (request.getType().toLowerCase()) {
            case "name" -> {
                books = bookRepository.searchByName(request.getKeyword(), pageable);
                total = bookRepository.countByName(request.getKeyword());
            }
            case "author" -> {
                books = bookRepository.searchByAuthor(request.getKeyword(), pageable);
                total = bookRepository.countByAuthor(request.getKeyword());
            }
            case "category" -> {
                books = bookRepository.searchByCategory(request.getKeyword(), pageable);
                total = bookRepository.countByCategory(request.getKeyword());
            }
            case "publisher" -> {
                books = bookRepository.searchByPublisher(request.getKeyword(), pageable);
                total = bookRepository.countByPublisher(request.getKeyword());
            }
            default -> throw new IllegalArgumentException("Invalid search type: " + request.getType());
        }

        if (request.getKeyword() == null || request.getKeyword().isBlank()) {
            books = bookRepository.findAll(pageable).toList();
            total = bookRepository.count();
        }

        List<BookResponse> responses = books.stream()
                .map(bookMapper::toBookResponse)
                .toList();

        return new PageImpl<>(responses, pageable, total);
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
