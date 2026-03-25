package com.rb.paylode.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SubscriptionDTO {

    private Long id;

    @NotNull(message = "User id is Mandatory")
    private Long userId;

    private String userName;

    private String userEmail;

    @NotNull(message = "Plan id is Mandatory")
    private Long planId;

    private String planName;

    private String planCode;

    private Long price;

    private String currency;

    private LocalDate startDate;

    private LocalDate endDate;

    private Boolean isActive;

    private Integer maxBooksAllowed;

    private Integer maxDaysPerBook;

    private Boolean autoRenew;

    private LocalDateTime cancelledAt;

    private String cancellationReason;

    private String notes;

    private Long dateRemaining;

    private Boolean isValid;

    private Boolean isExpired;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
