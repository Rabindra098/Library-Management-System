package com.rb.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rb.paylode.dto.SubscriptionPlanDTO;
import com.rb.paylode.response.APIResponse;
import com.rb.service.SubscriptionPlanService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/subscription-plan")
public class SubscriptionPlanController {
	
	private final SubscriptionPlanService planService;
	
	@GetMapping
	public ResponseEntity<?> getAllSubscriptionPlans() {
		List<SubscriptionPlanDTO> plans = planService.getAllSubscriptionPlans();
		return ResponseEntity.ok(plans);
	}
	@PostMapping("/admin/create")
	public ResponseEntity<?> createSubscriptionPlans(
	        @RequestBody @Valid SubscriptionPlanDTO dto) throws Exception {
	    return ResponseEntity.ok(planService.createSubscriptionPlan(dto));
	}

	@PutMapping("/admin/{id}")
	public ResponseEntity<?> updateSubscriptionPlans(
			@Valid @RequestBody SubscriptionPlanDTO planDTO,
			@PathVariable long id) throws Exception {
		SubscriptionPlanDTO plan = planService.updateSubscriptionPlan(id, planDTO);
		return ResponseEntity.ok(plan);
	}
	
	@DeleteMapping("/admin/{id}")
	public ResponseEntity<?> deleteSubscriptionPlans(
			@PathVariable long id) throws Exception {
		planService.deleteSubscriptionPlan(id);
		APIResponse apiResponse = new APIResponse("Plan Deleted Succefully",true);
		return ResponseEntity.ok(apiResponse);
	}
	
	
}
