package dev.thangngo.lmssoftdreams.services.impl;

import dev.thangngo.lmssoftdreams.dtos.request.borrow.BorrowCreateRequest;
import dev.thangngo.lmssoftdreams.dtos.request.borrow.BorrowUpdateRequest;
import dev.thangngo.lmssoftdreams.dtos.response.borrow.BorrowResponse;
import dev.thangngo.lmssoftdreams.entities.BookCopy;
import dev.thangngo.lmssoftdreams.entities.Borrow;
import dev.thangngo.lmssoftdreams.enums.BookStatus;
import dev.thangngo.lmssoftdreams.enums.ErrorCode;
import dev.thangngo.lmssoftdreams.exceptions.AppException;
import dev.thangngo.lmssoftdreams.mappers.BorrowMapper;
import dev.thangngo.lmssoftdreams.repositories.bookcopy.BookCopyRepository;
import dev.thangngo.lmssoftdreams.repositories.borrows.BorrowRepository;
import dev.thangngo.lmssoftdreams.services.BorrowService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class BorrowServiceImpl implements BorrowService {

    private final BorrowRepository borrowRepository;
    private final BorrowMapper borrowMapper;
    private final BookCopyRepository bookCopyRepository;

    public BorrowServiceImpl(BorrowRepository borrowRepository,
                             BorrowMapper borrowMapper,
                             BookCopyRepository bookCopyRepository) {
        this.borrowRepository = borrowRepository;
        this.borrowMapper = borrowMapper;
        this.bookCopyRepository = bookCopyRepository;
    }

    @Override
    public BorrowResponse createBorrow(BorrowCreateRequest request) {
        validateDates(request.getBorrowDate(), request.getReturnDate());
        ensureBookCopyAvailable(request.getBookCopyId());

        Borrow borrow = borrowMapper.toBorrow(request);
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

        if (request.getBorrowDate() != null && request.getReturnDate() != null) {
            validateDates(request.getBorrowDate(), request.getReturnDate());
        }

        if (request.getBookCopyId() != null) {
            ensureBookCopyAvailable(request.getBookCopyId());
        }

        borrowMapper.updateBorrowFromDto(request, borrow);

        Borrow savedBorrow = borrowRepository.save(borrow);
        return borrowMapper.toBorrowResponse(savedBorrow);
    }

    @Override
    public List<BorrowResponse> getAllBorrows() {
        return borrowRepository.findAll()
                .stream()
                .map(borrowMapper::toBorrowResponse)
                .toList();
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
    }
}
