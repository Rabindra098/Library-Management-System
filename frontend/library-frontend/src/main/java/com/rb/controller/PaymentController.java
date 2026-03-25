package com.rb.controller;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rb.exception.PaymentException;
import com.rb.paylode.dto.PaymentDTO;
import com.rb.paylode.request.PaymentVerifyRequest;
import com.rb.paylode.response.APIResponse;
import com.rb.service.PaymentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payments")
public class PaymentController {
	
	private final PaymentService paymentService;
	
	@PostMapping("/verify")
	public ResponseEntity<?> verifyPayment(@Valid @RequestBody PaymentVerifyRequest request) throws Exception {
		try {
			PaymentDTO payment = paymentService.verifyPayment(request);
			return ResponseEntity.ok(payment);
		}catch(PaymentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new APIResponse(e.getMessage(),false));
		}
	}
	@GetMapping
	public ResponseEntity<?> getAllPayments(
	        @RequestParam(defaultValue = "0") int page,
	        @RequestParam(defaultValue = "10") int size,
	        @RequestParam(defaultValue = "createdAt") String sortBy,
	        @RequestParam(defaultValue = "DESC") String sortDir) {

		Sort sort = sortDir.equalsIgnoreCase("DESC")
	            ? Sort.by(sortBy).descending()
	            : Sort.by(sortBy).ascending();

	    Pageable pageable = PageRequest.of(page, size, sort);

	    Page<PaymentDTO> payments = paymentService.getAllPayments(pageable);

	    return ResponseEntity.ok(payments);
	}

	
}
