package com.rb.controller;

import com.rb.paylode.response.PaymentInitiateResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rb.paylode.dto.SubscriptionDTO;
import com.rb.paylode.response.APIResponse;
import com.rb.service.SubscriptionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/subscriptions")
public class SubscriptionController {

	private final SubscriptionService subscriptionService;

	@PostMapping("/subscribe")
	public ResponseEntity<?> subscribe( @RequestBody SubscriptionDTO subscriptionDTO) throws Exception {
		PaymentInitiateResponse dto = subscriptionService.subscribe(subscriptionDTO);
		return ResponseEntity.ok(dto);
	}

	@GetMapping("/admin")
	public ResponseEntity<Page<SubscriptionDTO>> getAllSubscriptions(
	        @RequestParam(defaultValue = "0") int page,
	        @RequestParam(defaultValue = "10") int size
	) {

	    Pageable pageable = PageRequest.of(page, size);
	    Page<SubscriptionDTO> subscriptions =
	            subscriptionService.getAllSubscriptions(pageable);

	    return ResponseEntity.ok(subscriptions);
	}


	@GetMapping("/user/active")
	public ResponseEntity<?> getUserActiveSubscription(@RequestParam(required = false) Long userId) throws Exception {
		SubscriptionDTO dto = subscriptionService.getUserActiveSubscription(userId);
		return ResponseEntity.ok(dto);
	}
	
//	@GetMapping("/user/active")
//	public ResponseEntity<?> getUserActiveSubscription() throws Exception {
//
//	    SubscriptionDTO subscription =
//	            subscriptionService.getUserActiveSubscription();
//
//	    if (subscription == null) {
//	        return ResponseEntity
//	                .status(HttpStatus.NOT_FOUND)
//	                .body("No active subscription found");
//	    }
//
//	    return ResponseEntity.ok(subscription);
//	}


	@GetMapping("/admin/deactivate-expired")
	public ResponseEntity<?> deactivateExpiredSubscription() throws Exception {
		subscriptionService.deactivateExpiredSubscriptions();
		APIResponse res = new APIResponse("Task Done", true);
		return ResponseEntity.ok(res);
	}

	@PostMapping("/cancle/{subscriptionId}")
	public ResponseEntity<?> cancleSubscription(
			@PathVariable Long subscriptionId, 
			@RequestParam(required = false) String reason)throws Exception {
		SubscriptionDTO subscription = subscriptionService.cancelSubscription(subscriptionId, reason);
		return ResponseEntity.ok(subscription);
	}
	
	@PostMapping("/activate")
	public ResponseEntity<?> activateSubscription(
			@RequestParam Long subscriptionId, 
			@RequestParam Long paymentId)throws Exception {
		SubscriptionDTO subscription = subscriptionService.activateSubscription(subscriptionId, paymentId);
		return ResponseEntity.ok(subscription);
	}

}
