package com.rb.mapper;

import com.rb.model.BookLoan;
import com.rb.paylode.dto.BookLoanDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Component
@RequiredArgsConstructor
public class BookLoanMapper {
    public BookLoanDTO toDTO(BookLoan bookLoan) {

        if (bookLoan == null) {
            return null;
        }

        BookLoanDTO dto = new BookLoanDTO();

        dto.setId(bookLoan.getId());

        // User information
        if (bookLoan.getUser() != null) {
            dto.setUserId(bookLoan.getUser().getId());
            dto.setUserName(bookLoan.getUser().getFullName());
            dto.setUserEmail(bookLoan.getUser().getEmail());
        }

        // Book information
        if (bookLoan.getBook() != null) {
            dto.setBookid(bookLoan.getBook().getId());
            dto.setBookTitle(bookLoan.getBook().getTitle());
            dto.setBookISBN(bookLoan.getBook().getIsbn());
            dto.setBookAuthors(bookLoan.getBook().getAuthor());
            dto.setBookCoverImage(bookLoan.getBook().getCoverImageUrl());
        }

        // Book loan details
        dto.setType(bookLoan.getType());
        dto.setStatus(bookLoan.getStatus());
        dto.setCheckoutDate(bookLoan.getCheckoutDate());
        dto.setDueDate(bookLoan.getDueDate());
        dto.setReturnDate(bookLoan.getReturnDate());
        dto.setRenewalCount(bookLoan.getRenewalCount());
        dto.setMaxRenewals(bookLoan.getMaxRenewals());
        dto.setNotes(bookLoan.getNotes());
        dto.setIsOverdue(bookLoan.getIsOverdue());
        dto.setOverdueDays(bookLoan.getOverdueDays());
        dto.setCreatedAt(bookLoan.getCreatedAt());
        dto.setUpdatedAt(bookLoan.getUpdatedAt());

        // Remaining days
        if (bookLoan.getDueDate() != null) {
            dto.setRemainingDays(
                    ChronoUnit.DAYS.between(LocalDate.now(), bookLoan.getDueDate())
            );
        }
        return dto;
    }

}
