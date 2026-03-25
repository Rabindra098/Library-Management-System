package com.rb.service.impl;

import com.rb.domain.BookLoanStatus;
import com.rb.mapper.BookReviewMapper;
import com.rb.model.Book;
import com.rb.model.BookLoan;
import com.rb.model.BookReview;
import com.rb.model.User;
import com.rb.paylode.dto.BookReviewDTO;
import com.rb.paylode.request.CreateReviewRequest;
import com.rb.paylode.request.UpdateReviewRequest;
import com.rb.paylode.response.PageResponse;
import com.rb.repository.BookLoanRepository;
import com.rb.repository.BookRepository;
import com.rb.repository.BookReviewRepository;
import com.rb.service.BookReviewService;
import com.rb.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookReviewServiceImpl implements BookReviewService {


    private final UserService userService;
    private final BookRepository bookRepository;
    private final BookReviewRepository bookReviewRepository;
    private final BookReviewMapper bookReviewMapper;
    private final BookLoanRepository bookLoanRepository;

    @Override
    public BookReviewDTO createReview(CreateReviewRequest request) throws Exception {
//        1. fetch the logedin user
        User user=userService.getCurrentUser();

//        2. validate book  exist
        Book book=bookRepository.findById(request.getBookId())
                .orElseThrow(()-> new Exception("Book not found"));

//        3. check if user has already reviewed the book
        if(bookReviewRepository.existsByUserIdAndBookId(user.getId(), book.getId())) {
            throw new Exception("You have already reviewed this book");
        }
//        4. check if user has read the book
        boolean hasReadBook=hasUserReadBook(user.getId(),book.getId());
        if(!hasReadBook) {
            throw new Exception("You do not have read this book");
        }
//        5. create the review
        BookReview bookReview=new BookReview();
        bookReview.setUser(user);
        bookReview.setBook(book);
        bookReview.setRating(request.getRating());
        bookReview.setReviewText(request.getReviewText());
        bookReview.setTitle(request.getTitle());
        BookReview savedBookReview= bookReviewRepository.save(bookReview);
        return bookReviewMapper.toDTO(savedBookReview);
    }



    @Override
    public BookReviewDTO updateReview(Long reviewId, UpdateReviewRequest request) throws Exception {
//        1. fetch the login user
        User user=userService.getCurrentUser();
//        2. find the review
        BookReview bookReview=bookReviewRepository.findById(reviewId)
                .orElseThrow(()-> new Exception("Review not found"));
//        3. check if logged user is the owner of the review
        if(!bookReview.getUser().getId().equals(user.getId())) {
            throw new Exception("You have not review this book");
        }
//        4. update review
        bookReview.setReviewText(request.getReviewText());
        bookReview.setTitle(request.getTitle());
        bookReview.setRating(request.getRating());
        BookReview savedBookReview= bookReviewRepository.save(bookReview);
        return bookReviewMapper.toDTO(savedBookReview);
    }

    @Override
    public void deleteReview(Long reviewId) throws Exception {
        User user=userService.getCurrentUser();
        BookReview bookReview=bookReviewRepository.findById(reviewId)
                .orElseThrow(()-> new Exception("Review not found with id "+reviewId));
        if(!bookReview.getUser().getId().equals(user.getId())) {
            throw new Exception("You can delete your own review");
        }
        bookReviewRepository.delete(bookReview);
    }

    @Override
    public PageResponse<BookReviewDTO> getReviewsByBookId(Long id, int page, int size) throws Exception {
        Book books=bookRepository.findById(id)
                .orElseThrow(()->new Exception("Book not found by id "+id));
        Pageable pageable= PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<BookReview> reviewPage=bookReviewRepository.findByBook(books, pageable);
        return convertToPageResponse(reviewPage);
    }

    private boolean hasUserReadBook(Long userId, Long bookId) {
        List<BookLoan> bookLoans=bookLoanRepository.findByBookId(bookId);
        return bookLoans
                .stream()
                .anyMatch(loan ->
                                loan.getUser().getId().equals(userId) &&
                                loan.getStatus()== BookLoanStatus.RETURNED
                        );
    }

    private PageResponse<BookReviewDTO> convertToPageResponse(Page<BookReview> reviewPage) {
        List<BookReviewDTO> reviewDTOS=reviewPage.getContent()
                .stream()
                .map(bookReviewMapper::toDTO)
                .collect(Collectors.toList());
        return new PageResponse<>(
                reviewDTOS,
                reviewPage.getNumber(),
                reviewPage.getSize(),
                reviewPage.getTotalElements(),
                reviewPage.getTotalPages(),
                reviewPage.isLast(),
                reviewPage.isFirst(),
                reviewPage.isEmpty()
        );
    }
}
