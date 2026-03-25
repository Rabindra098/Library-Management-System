package com.rb.model;

import com.rb.domain.FineStatus;
import com.rb.domain.FineType;
import jakarta.persistence.*;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Fine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(nullable = false)
    private BookLoan bookLoan;

    private FineType type;

    @Column(nullable = false)
    private Long amount;

    private FineStatus status;

    @Column(nullable = false)
    private String reason;

    @Column(nullable = false)
    private String notes;

    @ManyToOne
    private User waivedBy;

    @Column
    private LocalDateTime waivedAt;

    @Column(length = 500)
    private String waiverReason;

    // Payment tracking
    @Column
    private LocalDateTime paidAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private User processedBy;


    @Column(length = 100)
    private String transactionId;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(nullable = false)
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public void applyPayment(Long paymentAmount) {
        if (paymentAmount == null || paymentAmount <= 0) {
            throw new IllegalArgumentException("Payment amount must be positive");
        }
        // Update status based on amount paid
            this.status = FineStatus.PAID;
            this.paidAt = LocalDateTime.now();
    }

    public void waive(User admin, String reason) {
        this.status = FineStatus.WAIVED;
        this.waivedBy = admin;
        this.waivedAt = LocalDateTime.now();
        this.waiverReason = reason;
    }



}
