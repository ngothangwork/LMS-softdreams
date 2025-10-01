package dev.thangngo.lmssoftdreams.services.impl;

import dev.thangngo.lmssoftdreams.dtos.request.borrow.BorrowCreateRequest;
import dev.thangngo.lmssoftdreams.dtos.request.borrow.BorrowUpdateRequest;
import dev.thangngo.lmssoftdreams.dtos.response.bookcopy.BookCopyListResponse;
import dev.thangngo.lmssoftdreams.dtos.response.bookcopy.BookCopyResponse;
import dev.thangngo.lmssoftdreams.dtos.response.borrow.BorrowResponse;
import dev.thangngo.lmssoftdreams.entities.Book;
import dev.thangngo.lmssoftdreams.entities.BookCopy;
import dev.thangngo.lmssoftdreams.entities.Borrow;
import dev.thangngo.lmssoftdreams.enums.BookStatus;
import dev.thangngo.lmssoftdreams.enums.BorrowStatus;
import dev.thangngo.lmssoftdreams.enums.ErrorCode;
import dev.thangngo.lmssoftdreams.exceptions.AppException;
import dev.thangngo.lmssoftdreams.mappers.BorrowMapper;
import dev.thangngo.lmssoftdreams.repositories.BookCopyRepository;
import dev.thangngo.lmssoftdreams.repositories.BookRepository;
import dev.thangngo.lmssoftdreams.repositories.BorrowRepository;
import dev.thangngo.lmssoftdreams.services.BorrowService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@Transactional

public class BorrowServiceImpl implements BorrowService {

    private final BorrowRepository borrowRepository;
    private final BorrowMapper borrowMapper;
    private final BookCopyRepository bookCopyRepository;
    private final BookRepository bookRepository;

    public BorrowServiceImpl(BorrowRepository borrowRepository,
                             BorrowMapper borrowMapper,
                             BookCopyRepository bookCopyRepository, BookRepository bookRepository) {
        this.borrowRepository = borrowRepository;
        this.borrowMapper = borrowMapper;
        this.bookCopyRepository = bookCopyRepository;
        this.bookRepository = bookRepository;
    }

    @Override
    public BorrowResponse createBorrow(BorrowCreateRequest request) {
        validateDates(request.getBorrowDate(), request.getReturnDate());
        bookRepository.findById(request.getBookId()).orElseThrow(() -> new AppException(ErrorCode.BOOK_NOT_FOUND));
        List<BookCopyListResponse> bookCopies = bookCopyRepository.findAllBookCopyByBookIdAndStatus(request.getBookId(), BookStatus.AVAILABLE.name());
        if (bookCopies.isEmpty()) {
            throw new AppException(ErrorCode.BOOK_COPY_NOT_FOUND_OR_NOT_AVAILABLE);
        }
        Borrow borrow = borrowMapper.toBorrow(request);
        borrow.setStatus(BorrowStatus.REQUESTED);
        Borrow savedBorrow = borrowRepository.save(borrow);

        return borrowMapper.toBorrowResponse(savedBorrow);
    }

    @Override
    public BorrowResponse getBorrowById(Long id) {
        Borrow borrow = borrowRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.BORROW_NOT_FOUND));

        return borrowMapper.toBorrowResponse(borrow);
    }

    @Override
    public BorrowResponse updateBorrow(Long id, BorrowUpdateRequest request) {
        Borrow borrow = borrowRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.BORROW_NOT_FOUND));

        if (request.getBookCopyId() != null) {
            ensureBookCopyAvailable(request.getBookCopyId());
            BookCopy bookCopy = bookCopyRepository.findById(request.getBookCopyId())
                    .orElseThrow(() -> new AppException(ErrorCode.BOOK_COPY_NOT_FOUND));

            if (request.getStatus() == BorrowStatus.BORROWED) {
                bookCopy.setStatus(BookStatus.UNAVAILABLE);
                bookCopyRepository.save(bookCopy);
            }
            borrow.setBookCopy(bookCopy);
        }
        borrowMapper.updateBorrowFromDto(request, borrow);
        Borrow savedBorrow = borrowRepository.save(borrow);
        return borrowMapper.toBorrowResponse(savedBorrow);
    }


    @Override
    public List<BorrowResponse> getAllBorrows() {
        return borrowRepository.findAllBorrowResponses();
    }

    @Override
    public BorrowResponse updateBorrowStatus(Long id, String status) {
        Borrow borrow = borrowRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.BORROW_NOT_FOUND));
        borrow.setStatus(BorrowStatus.valueOf(status));
        Borrow savedBorrow = borrowRepository.save(borrow);
        return borrowMapper.toBorrowResponse(savedBorrow);
    }

    private void ensureBookCopyAvailable(Long bookCopyId) {
        BookCopy bookCopy = bookCopyRepository.findById(bookCopyId)
                .orElseThrow(() -> new AppException(ErrorCode.BOOK_COPY_NOT_FOUND));

        if (bookCopy.getStatus() == BookStatus.UNAVAILABLE) {
            throw new AppException(ErrorCode.BOOK_COPY_ALREADY_BORROWED);
        }
    }

    private void validateDates(LocalDate start, LocalDate end) {
        if (start == null || end == null) {
            throw new IllegalArgumentException("Start and end date must not be null");
        }
        if (start.isAfter(end)) {
            throw new AppException(ErrorCode.START_DATE_AFTER_END_DATE);
        }
        if (start.isBefore(LocalDate.now())) {
            throw new AppException(ErrorCode.START_DATE_BEFORE_CURRENT_DATE);
        }
        long daysBetween = ChronoUnit.DAYS.between(start, end);
        if (daysBetween > 7) {
            throw new AppException(ErrorCode.END_DATE_TOO_FAR);
        }
    }
}
