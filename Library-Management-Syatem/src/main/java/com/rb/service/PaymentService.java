package com.rb.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.rb.exception.PaymentException;
import com.rb.paylode.dto.PaymentDTO;
import com.rb.paylode.request.PaymentInitiateRequest;
import com.rb.paylode.request.PaymentVerifyRequest;
import com.rb.paylode.response.PaymentInitiateResponse;

public interface PaymentService {
	PaymentInitiateResponse initiatePayemnt(PaymentInitiateRequest req) throws PaymentException;
	PaymentDTO verifyPayment(PaymentVerifyRequest req) throws Exception;
	Page<PaymentDTO> getAllPayments(Pageable pageable);

	
}
