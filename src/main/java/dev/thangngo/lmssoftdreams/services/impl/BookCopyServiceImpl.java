package dev.thangngo.lmssoftdreams.services.impl;

import dev.thangngo.lmssoftdreams.dtos.request.bookcopy.BookCopyCreateRequest;
import dev.thangngo.lmssoftdreams.dtos.request.bookcopy.BookCopyUpdateRequest;
import dev.thangngo.lmssoftdreams.dtos.response.bookcopy.BookCopyResponse;
import dev.thangngo.lmssoftdreams.entities.Book;
import dev.thangngo.lmssoftdreams.entities.BookCopy;
import dev.thangngo.lmssoftdreams.enums.ErrorCode;
import dev.thangngo.lmssoftdreams.exceptions.AppException;
import dev.thangngo.lmssoftdreams.mappers.BookCopyMapper;
import dev.thangngo.lmssoftdreams.repositories.bookcopy.BookCopyRepository;
import dev.thangngo.lmssoftdreams.repositories.books.BookRepository;
import dev.thangngo.lmssoftdreams.services.BookCopyService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class BookCopyServiceImpl implements BookCopyService {

    private final BookCopyRepository bookCopyRepository;
    private final BookRepository bookRepository;
    private final BookCopyMapper bookCopyMapper;

    public BookCopyServiceImpl(BookCopyRepository bookCopyRepository,
                               BookRepository bookRepository,
                               BookCopyMapper bookCopyMapper) {
        this.bookCopyRepository = bookCopyRepository;
        this.bookRepository = bookRepository;
        this.bookCopyMapper = bookCopyMapper;
    }

    @Override
    public BookCopyResponse createBookCopy(BookCopyCreateRequest request) {
        if (bookCopyRepository.existsByBarcode(request.getBarcode())) {
            throw new AppException(ErrorCode.BARCODE_ALREADY_EXISTS);
        }

        Book book = bookRepository.findById(request.getBookId())
                .orElseThrow(() -> new AppException(ErrorCode.BOOK_NOT_FOUND));

        BookCopy bookCopy = bookCopyMapper.toBookCopy(request);
        bookCopy.setBook(book);

        return bookCopyMapper.toBookCopyResponse(bookCopyRepository.save(bookCopy));
    }

    @Override
    public BookCopyResponse updateBookCopy(Long id, BookCopyUpdateRequest request) {
        BookCopy bookCopy = bookCopyRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.BOOK_COPY_NOT_FOUND));
        bookCopyMapper.updateBookCopyFromDto(request, bookCopy);
        return bookCopyMapper.toBookCopyResponse(bookCopyRepository.save(bookCopy));
    }

    @Override
    public void deleteBookCopy(Long id) {
        BookCopy bookCopy = bookCopyRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.BOOK_COPY_NOT_FOUND));
        bookCopyRepository.delete(bookCopy);
    }

    @Override
    public List<BookCopyResponse> getAllBookCopies() {
        return bookCopyRepository.findAll()
                .stream()
                .map(bookCopyMapper::toBookCopyResponse)
                .toList();
    }

    @Override
    public BookCopyResponse getBookCopyById(Long id) {
        return bookCopyRepository.findById(id)
                .map(bookCopyMapper::toBookCopyResponse)
                .orElseThrow(() -> new AppException(ErrorCode.BOOK_COPY_NOT_FOUND));
    }
}
