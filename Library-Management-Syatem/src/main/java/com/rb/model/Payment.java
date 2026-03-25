package com.rb.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.rb.domain.PaymentGateway;
import com.rb.domain.PaymentStatus;
import com.rb.domain.PaymentType;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Payment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	private User user;

	@ManyToOne
	private Subscription subscription;

    @Enumerated(EnumType.STRING)
	private PaymentType paymentType;

    @Enumerated(EnumType.STRING)
	private PaymentStatus status;

	@Enumerated(EnumType.STRING)
	private PaymentGateway gateway;

	private Long amount;

	private String transactionId;

	private String gatewayPaymentId;

	private String gatewayOrderId;

	private String gatewaySignature;

	private String description;

	private String failureReason;

	@CreationTimestamp
	private LocalDateTime initiatedAt;

	private LocalDateTime completedAt;

	@CreationTimestamp
	private LocalDateTime createdAt;

	@UpdateTimestamp
	private LocalDateTime updatedAt;
}
