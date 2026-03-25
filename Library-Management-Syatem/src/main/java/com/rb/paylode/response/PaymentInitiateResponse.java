package com.rb.paylode.response;

import com.rb.domain.PaymentGateway;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentInitiateResponse {

	private Long paymentid;
	
	private PaymentGateway gateway;
	
	private String transactionId;
	
	private String razorpayOrderId;
	
	private Long amount;
	
	private String description;
	
	private String checkoutUrl;
	
	private String message;
	
	private Boolean success;
}
