package com.rb.service;

import com.rb.paylode.dto.BookReviewDTO;
import com.rb.paylode.request.CreateReviewRequest;
import com.rb.paylode.request.UpdateReviewRequest;
import com.rb.paylode.response.PageResponse;
import org.springframework.data.domain.PageRequest;

public interface BookReviewService {
    BookReviewDTO createReview(CreateReviewRequest request) throws Exception;
    BookReviewDTO updateReview(Long reviewId, UpdateReviewRequest request) throws Exception;
    void deleteReview(Long reviewId) throws Exception;
    PageResponse<BookReviewDTO> getReviewsByBookId(Long id,int page,int size) throws Exception;
}
