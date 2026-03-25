package com.rb.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.rb.domain.PaymentGateway;
import com.rb.domain.PaymentType;
import com.rb.paylode.request.PaymentInitiateRequest;
import com.rb.paylode.response.PaymentInitiateResponse;
import com.rb.service.PaymentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.rb.exception.SubscriptionException;
import com.rb.mapper.SubscriptionMapper;
import com.rb.model.Subscription;
import com.rb.model.SubscriptionPlan;
import com.rb.model.User;
import com.rb.paylode.dto.SubscriptionDTO;
import com.rb.repository.SubscriptionPlanRepository;
import com.rb.repository.SubscriptionRepository;
import com.rb.service.SubscriptionService;
import com.rb.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionMapper subscriptionMapper;
    private final SubscriptionPlanRepository planRepository;
    private final UserService userService;
    private final PaymentService paymentService;

    @Override
    public PaymentInitiateResponse subscribe(SubscriptionDTO subscriptionDTO) throws Exception {
        User user = userService.getCurrentUser();
        SubscriptionPlan plan = planRepository.findById(subscriptionDTO.getPlanId())
                .orElseThrow(() -> new Exception("Plan not found"));

        Subscription subscription = subscriptionMapper.toEntity(subscriptionDTO, plan, user);
        subscription.initializeFromPlan();
        subscription.setIsActive(false);
        Subscription saveSubscription = subscriptionRepository.save(subscription);
//      create payment (todo)
        new PaymentInitiateRequest();
        PaymentInitiateRequest paymentInitiateRequest = PaymentInitiateRequest.builder()
                .userId(user.getId())
                .subscriptionId(subscription.getId())
                .paymentType(PaymentType.MEMBERSHIP)
                .gateway(PaymentGateway.RAZORPAY)
                .amount(subscription.getPrice())
                .description("Library Subscription - " + plan.getName())
                .build();

        return paymentService.initiatePayemnt(paymentInitiateRequest);
    }

    @Override
    public SubscriptionDTO getUserActiveSubscription(Long userId) throws Exception {
        User user = userService.getCurrentUser();
        Subscription subscription = subscriptionRepository.findActiveSubscriptionByUserId(user.getId(), LocalDate.now())
                .orElseThrow(() -> new SubscriptionException("No active subscription found"));
        return subscriptionMapper.toDTO(subscription);
    }
//    @Override
//    public SubscriptionDTO getUserActiveSubscription() throws Exception {
//        User user = userService.getCurrentUser();
//
//        return subscriptionRepository
//                .findActiveSubscriptionByUserId(user.getId(), LocalDate.now())
//                .map(subscriptionMapper::toDTO)
//                .orElse(null);
//    }


    @Override
    public SubscriptionDTO cancelSubscription(Long subscriptionId, String reason) throws SubscriptionException {

        Subscription subscription = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new SubscriptionException("Subscription not found with ID: " + subscriptionId));

        if (!Boolean.TRUE.equals(subscription.getIsActive())) {
            throw new SubscriptionException("Subscription is already inactive");
        }

        subscription.setCancelledAt(LocalDateTime.now());
        subscription.setCancellationReason(reason != null ? reason : "Cancelled by user");
        subscription.setIsActive(false);


        subscription = subscriptionRepository.save(subscription);
        return subscriptionMapper.toDTO(subscription);
    }

    @Override
    public SubscriptionDTO activateSubscription(Long subscriptionId, Long paymentId) throws SubscriptionException {

        Subscription subscription = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new SubscriptionException("Subscription not found with ID: " + subscriptionId));

        // TODO: verify payment using paymentId

        subscription.setIsActive(true);
        subscription = subscriptionRepository.save(subscription);

        return subscriptionMapper.toDTO(subscription);
    }

    @Override
    public Page<SubscriptionDTO> getAllSubscriptions(Pageable pageable) {

        Page<Subscription> page = subscriptionRepository.findAll(pageable);

        return page.map(subscriptionMapper::toDTO);
    }


    @Override
    public void deactivateExpiredSubscriptions() throws SubscriptionException {
        List<Subscription> expiredSubscriptions = subscriptionRepository
                .findExpiredActiveSubscriptions(LocalDate.now());
        for (Subscription subscription : expiredSubscriptions) {
            subscription.setIsActive(false);
            subscriptionRepository.save(subscription);
        }
    }

}
