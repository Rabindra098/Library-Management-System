package com.rb.service.impl;

import com.rb.domain.BookLoanStatus;
import com.rb.domain.BookLoanType;
import com.rb.exception.BookException;
import com.rb.mapper.BookLoanMapper;
import com.rb.model.Book;
import com.rb.model.BookLoan;
import com.rb.model.User;
import com.rb.paylode.dto.BookLoanDTO;
import com.rb.paylode.dto.SubscriptionDTO;
import com.rb.paylode.request.BookLoanSearchRequest;
import com.rb.paylode.request.CheckinRequest;
import com.rb.paylode.request.CheckoutRequest;
import com.rb.paylode.request.RenewalRequest;
import com.rb.paylode.response.PageResponse;
import com.rb.repository.BookLoanRepository;
import com.rb.repository.BookRepository;
import com.rb.service.BookLoanService;
import com.rb.service.SubscriptionService;
import com.rb.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookLoanServiceImpl implements BookLoanService {

    private final BookLoanRepository bookLoanRepository;
    private final UserService userService;
    private final SubscriptionService subscriptionService;
    private final BookRepository bookRepository;
    private final BookLoanMapper bookLoanMapper;

    @Override
    public BookLoanDTO checkoutBook(CheckoutRequest checkoutRequest) throws Exception {
        User user = userService.getCurrentUser();
        return checkoutBookForUser(user.getId(), checkoutRequest);
    }

    @Override
    public BookLoanDTO checkoutBookForUser(Long userId, CheckoutRequest checkoutRequest) throws Exception {
//      1.  Validate user exist
        User user = userService.findById(userId);
//      2.  validate user has active subscription
        SubscriptionDTO subscription = subscriptionService.getUserActiveSubscription(user.getId());
//      3.  validate book exists and is available
        Book book = bookRepository.findById(checkoutRequest.getBookId()).orElseThrow(() -> new Exception("book not find with id" + checkoutRequest.getBookId()));
        if (!book.getActive()) throw new Exception("book not active");
        if (book.getAvailableCopies() <= 0) throw new Exception("book not available");
//      4. check if user already has this book checkOut
        if (bookLoanRepository.hasActiveCheckout(userId, book.getId()))
            throw new BookException("book already has active checkout");
//      5. check user's active checkout limit
        long activeCheckouts = bookLoanRepository.countActiveBookLoansByUser(userId);
        int maxBooksAllowed = subscription.getMaxBooksAllowed();
        if (activeCheckouts > maxBooksAllowed)
            throw new BookException("You have reached the maximum number of books allowed for this checkout");
//      6. Check for overdue books
        long overdueCount = bookLoanRepository.countOverdueBookLoansByUser(userId);
        if (overdueCount > 0) throw new Exception("First return old overdue book!");
//       7. create book lone
        BookLoan bookLoan = BookLoan.builder()
                .user(user)
                .book(book)
                .type(BookLoanType.CHECKOUT)
                .status(BookLoanStatus.CHECKED_OUT)
                .checkoutDate(LocalDate.now())
                .dueDate(LocalDate.now().plusDays(checkoutRequest.getCheckoutDays())).
                renewalCount(0)
                .maxRenewals(2).
                isOverdue(false)
                .overdueDays(0)

                .build();
//      8. update book available copies
        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookRepository.save(book);
//       9. save book lone
        BookLoan savedBookLoan = bookLoanRepository.save(bookLoan);

        return bookLoanMapper.toDTO(savedBookLoan);
    }

    @Override
    public BookLoanDTO checkinBook(CheckinRequest checkinRequest) throws Exception {
//        1. validate bookloan exist
        BookLoan bookLoan = bookLoanRepository.findById(checkinRequest.getBookLoanId()).orElseThrow(() -> new Exception("book loan not found"));
//        2. check if already returned
        if (!bookLoan.isActive()) throw new BookException("book loan is not active");
        bookLoan.setReturnDate(LocalDate.now());
//    4.
        BookLoanStatus condition = checkinRequest.getCondition();
        if (condition == null) condition = BookLoanStatus.RETURNED;
        bookLoan.setStatus(condition);
//    5. fine todo
        bookLoan.setOverdueDays(0);
        bookLoan.setIsOverdue(false);
//        6.
        bookLoan.setNotes("Book returned by user");
//        7. update book available
        if (condition != BookLoanStatus.LOST) {
            Book book = bookLoan.getBook();
            book.setAvailableCopies(book.getAvailableCopies() + 1);
            bookRepository.save(book);
//         process next reservation todo
        }
//      8.
        BookLoan savedBookLoan = bookLoanRepository.save(bookLoan);
        return bookLoanMapper.toDTO(savedBookLoan);
    }

    @Override
    public BookLoanDTO renewCheckout(RenewalRequest renewalRequest) throws Exception {
//        1. validate book loan exist
        BookLoan bookLoan = bookLoanRepository.findById(renewalRequest.getBookLoanId()).orElseThrow(() -> new Exception("book loan not found"));
//        2. check if can be renewed
        if (!bookLoan.canRenew()) throw new BookException("book loan can't renew");
//        update due date
        bookLoan.setDueDate(bookLoan.getDueDate().plusDays(renewalRequest.getExtensionDays()));

        bookLoan.setRenewalCount(bookLoan.getRenewalCount() + 1);
        bookLoan.setNotes("Book renewed by user");
        BookLoan savedBookLoan = bookLoanRepository.save(bookLoan);
        return bookLoanMapper.toDTO(savedBookLoan);
    }

    @Override
    public PageResponse<BookLoanDTO> getMyBookLoans(BookLoanStatus status, int page, int size) throws Exception {
        User currentUser = userService.getCurrentUser();
        Page<BookLoan> bookLoanPage;
        if (status != null) {
            //return only active checkouts, sorted by due date
            Pageable pageable = PageRequest.of(page, size, Sort.by("dueDate").ascending());
            bookLoanPage = bookLoanRepository.findByStatusAndUser(status, currentUser, pageable);
        } else {
//            return all history(both active and return) sorted by creation date description
            Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").ascending());
            bookLoanPage = bookLoanRepository.findByUserId(currentUser.getId(), pageable);
        }
        return convertToPageResponse(bookLoanPage);
    }

    @Override
    public PageResponse<BookLoanDTO> getBookLoans(BookLoanSearchRequest searchRequest) {
        //Build pageable with sorting, size, etc
        Pageable pageable = createPageable(searchRequest.getPage(), searchRequest.getSize(), searchRequest.getSortBy(), searchRequest.getSortDirection());
        Page<BookLoan> bookLoanPage;
//      apply filtering logic dynamically
        if (Boolean.TRUE.equals(searchRequest.getOverdueOnly())) {
            //fetch overdue loans
            bookLoanPage = bookLoanRepository.findOverdueBookLoans(LocalDate.now(), pageable);
        } else if (searchRequest.getUserId() != null) {
            // fetch loan by specific user
            bookLoanPage = bookLoanRepository.findByUserId(searchRequest.getUserId(), pageable);
        } else if (searchRequest.getBookId() != null) {
            // fetch loan by specific book
            bookLoanPage = bookLoanRepository.findByBookId(searchRequest.getBookId(), pageable);
        } else if (searchRequest.getStatus() != null) {
            // fetch loan by status
            bookLoanPage = bookLoanRepository.findByStatus(searchRequest.getStatus(), pageable);
        } else if (searchRequest.getStartDate() != null && searchRequest.getEndDate() != null) {
            //fetch loan within date range
            bookLoanPage = bookLoanRepository.findBookLoansByDateRange(searchRequest.getStartDate(), searchRequest.getEndDate(), pageable);
        } else {
            bookLoanPage = bookLoanRepository.findAll(pageable);
        }
        return convertToPageResponse(bookLoanPage);
    }

    @Override
    public int updateOverdueBookLoan() {

        Pageable pageable = PageRequest.of(0, 1000);
        Page<BookLoan> overduePage = bookLoanRepository.findOverdueBookLoans(LocalDate.now(), pageable);

        int updateCount = 0;

        for (BookLoan bookLoan : overduePage.getContent()) {

            if (bookLoan.getStatus() == BookLoanStatus.CHECKED_OUT) {

                bookLoan.setStatus(BookLoanStatus.OVERDUE);
                bookLoan.setIsOverdue(true);
//          calculate overdue date
                int overdueDays = calculateOverDueDate(bookLoan.getDueDate(), LocalDate.now());

//                BigDecimal fine =
//                        fineCalculationService.calculateOverdueFine(bookLoan);

                bookLoanRepository.save(bookLoan);
                updateCount++;
            }
        }

        return updateCount;
    }
    private Pageable createPageable(int page, int size, String sortBy, String sortDirection) {
        size = Math.min(size, 100);
        size = Math.max(size, 1);
        Sort sort = sortDirection.equalsIgnoreCase("ASC") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        return PageRequest.of(page, size, sort);
    }

    private PageResponse<BookLoanDTO> convertToPageResponse(Page<BookLoan> bookLoanPage) {
        List<BookLoanDTO> bookLoanDTOlist = bookLoanPage.getContent().stream().map(bookLoanMapper::toDTO).collect(Collectors.toList());
        return new PageResponse<>(bookLoanDTOlist, bookLoanPage.getNumber(), bookLoanPage.getSize(), bookLoanPage.getTotalElements(), bookLoanPage.getTotalPages(), bookLoanPage.isLast(), bookLoanPage.isFirst(), bookLoanPage.isEmpty());
    }
//      29/01
//      20/01
    public int calculateOverDueDate(LocalDate dueDate, LocalDate today) {
        if (today.isBefore(dueDate) || today.isEqual(dueDate)) {
            return 0;
        }
        return (int) ChronoUnit.DAYS.between(dueDate, today);
    }
}
