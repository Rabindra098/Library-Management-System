package com.rb.paylode.request;

import com.rb.domain.BookLoanStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CheckinRequest {
    @NotNull(message = "Book lone id is mandatory")
    private Long bookLoanId;

    private BookLoanStatus condition=BookLoanStatus.RETURNED;

    private String notes;
}
