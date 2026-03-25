package com.rb.controller;

import com.rb.paylode.dto.BookReviewDTO;
import com.rb.paylode.request.CreateReviewRequest;
import com.rb.paylode.request.UpdateReviewRequest;
import com.rb.paylode.response.APIResponse;
import com.rb.paylode.response.PageResponse;
import com.rb.service.BookReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reviews")
public class BookReviewController {
    private final BookReviewService bookReviewService;

    @PostMapping
    public ResponseEntity<?> createReview(
            @Valid @RequestBody CreateReviewRequest request) throws Exception{
        BookReviewDTO reviewDTO=bookReviewService.createReview(request);
        return ResponseEntity.ok().body(reviewDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateReview(
            @PathVariable Long id,
            @Valid @RequestBody UpdateReviewRequest request) throws Exception{
        BookReviewDTO reviewDTO=bookReviewService.updateReview(id,request);
        return ResponseEntity.ok().body(reviewDTO);
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<?> deleteReview(
            @PathVariable Long reviewId) throws Exception{
        bookReviewService.deleteReview(reviewId);
        return ResponseEntity.ok(new APIResponse("Review deleted Successfully",true));
    }

    @GetMapping("/book/{bookId}")
    public ResponseEntity<PageResponse<BookReviewDTO>> getReviewsByBook(
            @PathVariable Long bookId,
            @RequestParam(defaultValue = "0")int page,
            @RequestParam(defaultValue = "10")int size) throws Exception{
        PageResponse<BookReviewDTO> reviews =bookReviewService
                .getReviewsByBookId(bookId,page,size);
        return ResponseEntity.ok(reviews);
    }
}
