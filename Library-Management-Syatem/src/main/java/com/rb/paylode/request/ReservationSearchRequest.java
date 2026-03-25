package com.rb.paylode.request;

import com.rb.domain.ReservationStatus;
import com.rb.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReservationSearchRequest {
    private Long userId;

    private Long bookId;

    private ReservationStatus status;

    private Boolean activeOnly;

    private int page;
    private int size;

    private String sortBy="reversedAt";
    private String sortDirection="DESC";
}
