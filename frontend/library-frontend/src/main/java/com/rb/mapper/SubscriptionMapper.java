package com.rb.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.rb.exception.SubscriptionException;
import com.rb.model.Subscription;
import com.rb.model.SubscriptionPlan;
import com.rb.model.User;
import com.rb.paylode.dto.SubscriptionDTO;
import com.rb.repository.SubscriptionPlanRepository;
import com.rb.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SubscriptionMapper {
	private final UserRepository userRepository;
	private final SubscriptionPlanRepository planRepository;

	public SubscriptionDTO toDTO(Subscription subscription) {
		if (subscription == null) {
			return null;
		}

		SubscriptionDTO dto = new SubscriptionDTO();
		dto.setId(subscription.getId());

		if (subscription.getUser() != null) {
			dto.setUserId(subscription.getUser().getId());
			dto.setUserName(subscription.getUser().getFullName());
			dto.setUserEmail(subscription.getUser().getEmail());
		}

		// Plan information
		if (subscription.getPlan() != null) {
			dto.setPlanId(subscription.getPlan().getId());
		}

		dto.setPlanName(subscription.getPlanName());
		dto.setPlanCode(subscription.getPlanCode());
		dto.setPrice(subscription.getPrice());

		dto.setStartDate(subscription.getStartDate());
		dto.setEndDate(subscription.getEndDate());
		dto.setIsActive(subscription.getIsActive());
		dto.setMaxBooksAllowed(subscription.getMaxBooksAllowed());
		dto.setMaxDaysPerBook(subscription.getMaxDaysPerBook());
		dto.setAutoRenew(subscription.getAutoRenew());
		dto.setCancelledAt(subscription.getCancelledAt());
		dto.setCancellationReason(subscription.getCancellationReason());
		dto.setNotes(subscription.getNotes());
		dto.setCreatedAt(subscription.getCreatedAt());
		dto.setUpdatedAt(subscription.getUpdatedAt());

		dto.setDateRemaining(subscription.getDaysRemaining());
		dto.setIsValid(subscription.isValid());
		dto.setIsExpired(subscription.isExpired());
		return dto;
	}

	public Subscription toEntity(SubscriptionDTO dto,
								SubscriptionPlan plan,
								User user) throws SubscriptionException {
		if (dto == null)
			return null;
		Subscription subscription = new Subscription();
		subscription.setId(dto.getId());
		subscription.setUser(user);
		subscription.setPlan(plan);
		subscription.setNotes(dto.getNotes());
		
		
/**
		// map user
		if (dto.getUserId() != null) {
			User user = userRepository.findById(dto.getUserId())
					.orElseThrow(() -> new SubscriptionException("User not found with id: " + dto));
			subscription.setUser(user);
		}
		// map plan
		if (dto.getPlanId() != null) {
			SubscriptionPlan plan = planRepository.findById(dto.getPlanId())
					.orElseThrow(() -> new SubscriptionException("Subscriptiplan is not found with id: " + dto));
			subscription.setPlan(plan);
		}
		subscription.setPlanName(dto.getPlanName());
		subscription.setPlanCode(dto.getPlanCode());
		subscription.setPrice(dto.getPrice());
		subscription.setStartDate(dto.getStartDate());
		subscription.setEndDate(dto.getEndDate());
		subscription.setIsActive(dto.getIsActive() != null ? dto.getIsActive() : true);
		subscription.setMaxBooksAllowed(dto.getMaxBooksAllowed());
		subscription.setMaxDaysPerBook(dto.getMaxDaysPerBook());
		subscription.setAutoRenew(dto.getAutoRenew() != null ? dto.getAutoRenew() : false);
		subscription.setCancelledAt(dto.getCancelledAt());
		subscription.setCancellationReason(dto.getCancellationReason());
		subscription.setNotes(dto.getNotes());
**/
		
		
		return subscription;
	}

	public List<SubscriptionDTO> toDTOList(List<Subscription> subscription) {
		if (subscription == null)
			return null;
		return subscription.stream().map(this::toDTO).collect(Collectors.toList());
	}

}
