package com.rb.service;

import com.rb.domain.BookLoanStatus;
import com.rb.paylode.dto.BookLoanDTO;
import com.rb.paylode.request.BookLoanSearchRequest;
import com.rb.paylode.request.CheckinRequest;
import com.rb.paylode.request.CheckoutRequest;
import com.rb.paylode.request.RenewalRequest;
import com.rb.paylode.response.PageResponse;

public interface BookLoanService {
    BookLoanDTO checkoutBook(CheckoutRequest checkoutRequest) throws Exception;
    BookLoanDTO checkoutBookForUser(Long userId, CheckoutRequest checkoutRequest) throws Exception;
    BookLoanDTO checkinBook(CheckinRequest checkinRequest) throws Exception;
    BookLoanDTO renewCheckout(RenewalRequest renewalRequest) throws Exception;
    PageResponse<BookLoanDTO> getMyBookLoans(BookLoanStatus status,int page,int size) throws Exception;
    PageResponse<BookLoanDTO> getBookLoans(BookLoanSearchRequest request);
    int updateOverdueBookLoan();
}
