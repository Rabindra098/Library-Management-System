package com.rb.controller;

import com.rb.domain.BookLoanStatus;
import com.rb.paylode.dto.BookLoanDTO;
import com.rb.paylode.request.BookLoanSearchRequest;
import com.rb.paylode.request.CheckinRequest;
import com.rb.paylode.request.CheckoutRequest;
import com.rb.paylode.request.RenewalRequest;
import com.rb.paylode.response.APIResponse;
import com.rb.paylode.response.PageResponse;
import com.rb.service.BookLoanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/book-loans")
public class BookLoanController {
    public final BookLoanService bookLoanService;

    @PostMapping("/checkout")
    public ResponseEntity<?> checkoutBook(@Valid @RequestBody CheckoutRequest checkoutRequest) throws Exception {
        BookLoanDTO bookLoanDTO = bookLoanService.checkoutBook(checkoutRequest);
        return new ResponseEntity<>(bookLoanDTO, HttpStatus.CREATED);
    }

    @PostMapping("/checkout/user/{userId}")
    public ResponseEntity<?> checkoutBookForUser(@PathVariable Long userId, @Valid @RequestBody CheckoutRequest checkoutRequest) throws Exception {
        BookLoanDTO bookLoanDTO = bookLoanService.checkoutBookForUser(userId, checkoutRequest);
        return new ResponseEntity<>(bookLoanDTO, HttpStatus.CREATED);
    }

    @PostMapping("/checkin")
    public ResponseEntity<?> checkin(@Valid @RequestBody CheckinRequest checkinRequest) throws Exception {
        BookLoanDTO bookLoanDTO = bookLoanService.checkinBook(checkinRequest);
        return new ResponseEntity<>(bookLoanDTO, HttpStatus.OK);
    }

    @PostMapping("/renew")
    public ResponseEntity<?> renew(@Valid @RequestBody RenewalRequest renewalRequest) throws Exception {
        BookLoanDTO bookLoanDTO = bookLoanService.renewCheckout(renewalRequest);
        return new ResponseEntity<>(bookLoanDTO, HttpStatus.OK);
    }

    @GetMapping("/my")
    public ResponseEntity<?> getMyBookLoans(
            @RequestParam(required = false) BookLoanStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) throws Exception {
        PageResponse<BookLoanDTO> bookLoans = bookLoanService.getMyBookLoans(status, page, size);
        return new ResponseEntity<>(bookLoans, HttpStatus.OK);
    }

    @PostMapping("/search")
    public ResponseEntity<?> getAllBookLoans(
            @RequestBody BookLoanSearchRequest searchRequest
    ) throws Exception {
        PageResponse<BookLoanDTO> bookLoans = bookLoanService.getBookLoans(searchRequest);
        return new ResponseEntity<>(bookLoans, HttpStatus.OK);
    }

    @PostMapping("/admin/update-overdue")
    public ResponseEntity<?> updateOverdueBookLoan() throws Exception {
        int updateCount = bookLoanService.updateOverdueBookLoan();
        return ResponseEntity.ok(new APIResponse("Over due Book loans are updated", true));
    }


}
