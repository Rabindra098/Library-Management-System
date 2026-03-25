package com.rb.service;

import com.rb.paylode.response.PaymentInitiateResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.rb.exception.SubscriptionException;
import com.rb.paylode.dto.SubscriptionDTO;

public interface SubscriptionService {

	PaymentInitiateResponse subscribe(SubscriptionDTO subscriptionDTO) throws Exception;

	SubscriptionDTO getUserActiveSubscription(Long userId) throws Exception;

    SubscriptionDTO cancelSubscription(Long subscriptionId, String reason) throws SubscriptionException;

    SubscriptionDTO activateSubscription(Long subscriptionId, Long paymentId) throws SubscriptionException;

    Page<SubscriptionDTO> getAllSubscriptions(Pageable pageable);

    void deactivateExpiredSubscriptions() throws SubscriptionException;
}
