package com.rb.repository;

import com.rb.model.Book;
import com.rb.model.WishList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WishListRepository extends JpaRepository<WishList, Long> {
    Page<WishList> findByUserId(Long userId, Pageable pageable);
    WishList findByUserIdAndBookId(Long userId,Long bookId);
    boolean existsByUserIdAndBookId(Long userId,Long bookId);
}
