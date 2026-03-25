package com.rb.service.impl;



import java.time.LocalDateTime;
import java.util.UUID;

import com.rb.domain.PaymentGateway;
import com.rb.event.publisher.PaymentEventPublisher;
import com.rb.mapper.PaymentMapper;
import com.rb.paylode.response.PaymentLinkResponse;
import com.rb.service.gateway.RazorpayService;
import org.json.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.rb.domain.PaymentStatus;
import com.rb.exception.PaymentException;
import com.rb.model.Payment;
import com.rb.model.Subscription;
import com.rb.model.User;
import com.rb.paylode.dto.PaymentDTO;
import com.rb.paylode.request.PaymentInitiateRequest;
import com.rb.paylode.request.PaymentVerifyRequest;
import com.rb.paylode.response.PaymentInitiateResponse;
import com.rb.repository.PaymentRepository;
import com.rb.repository.SubscriptionRepository;
import com.rb.repository.UserRepository;
import com.rb.service.PaymentService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService{

	private final UserRepository userRepository;
	private final SubscriptionRepository subscriptionRepository;
	private final PaymentRepository paymentRepository;
    private final RazorpayService razorpayService;
    private final PaymentMapper paymentMapper;
    private final PaymentEventPublisher paymentEventPublisher;

    @Override
	public PaymentInitiateResponse initiatePayemnt(PaymentInitiateRequest req) throws PaymentException {
		User user=userRepository.findById(req.getUserId()).get();
		Payment payment = new Payment();
		payment.setUser(user);
		payment.setPaymentType(req.getPaymentType());
		payment.setGateway(req.getGateway());
		payment.setAmount(req.getAmount());
		
		payment.setDescription(req.getDescription());
		payment.setStatus(PaymentStatus.SUCCESS);
		payment.setTransactionId("TXN_"+UUID.randomUUID());
		payment.setInitiatedAt(LocalDateTime.now());
		
		if(req.getSubscriptionId() != null) {
			Subscription sub=subscriptionRepository.findById(req.getSubscriptionId())
			.orElseThrow(()-> new PaymentException("Subscription not found"));
			payment.setSubscription(sub);
		}
		payment=paymentRepository.save(payment);
        PaymentInitiateResponse response=new PaymentInitiateResponse();
        if(req.getGateway() == PaymentGateway.RAZORPAY){
            PaymentLinkResponse paymentLinkResponse= razorpayService.createPayemntLink(user, payment);
            response=PaymentInitiateResponse.builder()
                    .paymentid(payment.getId())
                    .gateway(req.getGateway())
                    .checkoutUrl(paymentLinkResponse.getPayment_link_url())
                    .transactionId(paymentLinkResponse.getPayemnt_link_id())
                    .amount(payment.getAmount())
                    .description(payment.getDescription())
                    .success(true)
                    .message("Payment initiated successfully")
                    .build();
            payment.setGatewayOrderId(paymentLinkResponse.getPayemnt_link_id());
        }
        payment.setStatus(PaymentStatus.PROCESSING);
        paymentRepository.save(payment);
        //payment initiate event
		return response;
	}

	@Override
	public PaymentDTO verifyPayment(PaymentVerifyRequest req) throws Exception {
        JSONObject paymentDetails =razorpayService.fetchPaymentDetails(
                req.getRazorpayPaymentId()
        );
        JSONObject notes=paymentDetails.getJSONObject("notes");
        Long paymentId=Long.parseLong(notes.getString("payment_id"));

        Payment payment=paymentRepository.findById(paymentId).get();

        boolean isValid=razorpayService.isValidPayment(req.getRazorpayPaymentId());
        if(PaymentGateway.RAZORPAY == payment.getGateway()){
            if(isValid){
                payment.setGatewayOrderId(req.getRazorpayPaymentId());
            }
        }
        if(isValid){
            payment.setStatus(PaymentStatus.SUCCESS);
            payment.setCompletedAt(LocalDateTime.now());
            payment=paymentRepository.save(payment);
        }
        //publish payment success event - todo
        paymentEventPublisher.publishPaymentSuccessEvent(payment);

		return paymentMapper.toDTO(payment);
	}

	@Override
	public Page<PaymentDTO> getAllPayments(Pageable pageable) {
		Page<Payment> payments=paymentRepository.findAll(pageable);
		return payments.map(paymentMapper::toDTO);
	}

}
