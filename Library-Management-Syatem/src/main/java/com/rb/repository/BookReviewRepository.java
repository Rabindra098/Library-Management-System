package com.rb.repository;

import com.rb.model.Book;
import com.rb.model.BookReview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface  BookReviewRepository extends JpaRepository<BookReview, Long> {
    Page<BookReview> findByBook(Book book, Pageable pageable);
    boolean existsByUserIdAndBookId(Long userId, Long bookId);
}
