package com.rb.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.rb.mapper.SubscriptionPlanMapper;
import com.rb.model.SubscriptionPlan;
import com.rb.model.User;
import com.rb.paylode.dto.SubscriptionPlanDTO;
import com.rb.repository.SubscriptionPlanRepository;
import com.rb.service.SubscriptionPlanService;
import com.rb.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SubscriptionPlanServiceImpl implements SubscriptionPlanService {
	
	private final SubscriptionPlanRepository planRepository;
	private final SubscriptionPlanMapper planMapper;
	private final UserService userService;
	
    @Override
    public SubscriptionPlanDTO createSubscriptionPlan(SubscriptionPlanDTO planDTO) throws Exception {
    	if(planRepository.existsByPlanCode(planDTO.getPlanCode())) {
    		throw new  Exception("Plan Code Already exist");
    	}
    	SubscriptionPlan plan=planMapper.toEntity(planDTO);
    	User currentUser = userService.getCurrentUser();
    	
    	plan.setCreatedBy(currentUser.getFullName());
    	plan.setCurrency("INR");
    	SubscriptionPlan savedPlan = planRepository.save(plan);
        return planMapper.toDTO(savedPlan);
    }

    @Override
    public SubscriptionPlanDTO updateSubscriptionPlan(Long planId, SubscriptionPlanDTO planDTO) throws Exception {
    	SubscriptionPlan existingPlan = planRepository.findById(planId).orElseThrow(()->new Exception("Plan not found"));
    	planMapper.updateEntity(planDTO, existingPlan);
    	User currentUser = userService.getCurrentUser();
    	existingPlan.setCreatedBy(currentUser.getFullName());
//    	existingPlan.setCurrency("INR");
    	SubscriptionPlan updatedPlan = planRepository.save(existingPlan);
        return planMapper.toDTO(updatedPlan);
    }

    @Override
    public void deleteSubscriptionPlan(Long planId) throws Exception {
    	SubscriptionPlan existingPlan = planRepository.findById(planId).orElseThrow(()->new Exception("Plan not found"));
    	planRepository.delete(existingPlan);
    }

    @Override
    public List<SubscriptionPlanDTO> getAllSubscriptionPlans() {
    	List<SubscriptionPlan> planList=planRepository.findAll();
        return planList.stream().map(planMapper::toDTO).collect(Collectors.toList());
    }

	@Override
	public SubscriptionPlan getBySubscriptionPlanCode(String subscriptionPlanCode) throws Exception {
		SubscriptionPlan byPlanCode = planRepository.findByPlanCode(subscriptionPlanCode);
		if(byPlanCode == null)
			throw new Exception("Plan not found");
		return byPlanCode;
	}
}
