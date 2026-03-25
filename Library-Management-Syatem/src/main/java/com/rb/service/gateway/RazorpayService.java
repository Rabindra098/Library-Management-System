package com.rb.service.gateway;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.razorpay.PaymentLink;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.rb.domain.PaymentType;
import com.rb.exception.PaymentException;
import com.rb.model.Payment;
import com.rb.model.SubscriptionPlan;
import com.rb.model.User;
import com.rb.paylode.response.PaymentLinkResponse;
import com.rb.service.SubscriptionPlanService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RazorpayService {
	
	private final SubscriptionPlanService subscriptionPlanService;
	
	@Value("${razorpay.key.id:}")
	private String razorpayKeyId;

	@Value("${razorpay.key.secret:}")
	private String razorpayKeySecret;

	@Value("${razorpay.callback.base-url:http://localhost:5173}")
	private String callbackBaseUrl;

	public PaymentLinkResponse createPayemntLink(User user, Payment payment) throws PaymentException {
		try {

			RazorpayClient razorpayClient = new RazorpayClient(razorpayKeyId, razorpayKeySecret);
			Long amountInPaisa = payment.getAmount() * 100;

			JSONObject paymentLinkReq = new JSONObject();
            paymentLinkReq.put("amount", amountInPaisa);
            paymentLinkReq.put("currency", "INR");
            paymentLinkReq.put("description", payment.getDescription());

            JSONObject customer = new JSONObject();
			customer.put("name", user.getFullName());
			customer.put("email", user.getEmail());
			if (user.getPhone() != null)
				customer.put("contact", user.getPhone());

			paymentLinkReq.put("customer", customer);

			JSONObject notify = new JSONObject();
			notify.put("email", true);
			notify.put("sms", user.getPhone() != null);
			paymentLinkReq.put("notify", notify);

			paymentLinkReq.put("reminder_enable", true);
			// Callback configuration
			String successUrl = callbackBaseUrl + "/payment-success/" + payment.getId();

			paymentLinkReq.put("callback_url", successUrl);
			paymentLinkReq.put("callback_method", "get");

			JSONObject notes = new JSONObject();
			notes.put("user_id", user.getId());
			notes.put("payment_id", payment.getId());

			if (payment.getPaymentType() == PaymentType.MEMBERSHIP) {
				notes.put("subscription_id", payment.getSubscription().getId());
				notes.put("plan", payment.getSubscription().getPlan().getPlanCode());
				notes.put("type", PaymentType.MEMBERSHIP);
			} else if (payment.getPaymentType() == PaymentType.FINE) {
				// todo
//			    notes.put("fine_id", payment.getFine().getId());
				notes.put("type", PaymentType.FINE);
			}
			paymentLinkReq.put("notes", notes);

			PaymentLink paymentLink = razorpayClient.paymentLink.create(paymentLinkReq);
			String paymentUrl = paymentLink.get("short_url");
			String paymentLinkId = paymentLink.get("id");

			PaymentLinkResponse response = new PaymentLinkResponse();
			response.setPayemnt_link_id(paymentLinkId);
			response.setPayment_link_url(paymentUrl);
			return response;

		}catch (Exception e) {
            throw new PaymentException(
                    "Failed to create Razorpay payment link: " + e.getMessage()
            );
        }

    }

	public JSONObject fetchPaymentDetails(String paymentId) throws Exception {
		try {
			RazorpayClient razorpay = new RazorpayClient(razorpayKeyId, razorpayKeySecret);
			com.razorpay.Payment payment = razorpay.payments.fetch(paymentId);

			return payment.toJson();
		} catch (RazorpayException e) {
			throw new Exception("Failed to fetch payment details: " + e.getMessage(), e);
		}
	}
	public boolean isValidPayment(String paymentId) {
	    try {
	        JSONObject paymentDetails = fetchPaymentDetails(paymentId);

	        String status = paymentDetails.optString("status");
	        long amount = paymentDetails.optLong("amount");
	        long amountInRupees = amount / 100;

	        JSONObject notes = paymentDetails.getJSONObject("notes");
	        String paymentType = notes.optString("type");

	        // 1. Check status
	        if (!"captured".equalsIgnoreCase(status)) {
	            return false;
	        }

	        // 2. Check expected amount
	        if (paymentType.equals(PaymentType.MEMBERSHIP.toString())) {
	            String planCode = notes.optString("plan");
	            SubscriptionPlan subscriptionPlan =
	                    subscriptionPlanService.getBySubscriptionPlanCode(planCode);
	            
	            return amountInRupees == subscriptionPlan.getPrice();

	        } else if (paymentType.equals(PaymentType.FINE.toString())) {
	            Long fineId = notes.getLong("fine_id");
//				todo
	            
	        }
	        return false;

	    } catch (Exception e) {
	        return false;
	    }
	}


}
