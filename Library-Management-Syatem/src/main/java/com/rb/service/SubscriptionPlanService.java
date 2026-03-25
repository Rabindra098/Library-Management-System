package com.rb.service;

import com.rb.model.SubscriptionPlan;
import com.rb.paylode.dto.SubscriptionPlanDTO;

import java.util.List;

public interface SubscriptionPlanService {
    SubscriptionPlanDTO createSubscriptionPlan(SubscriptionPlanDTO planDTO) throws Exception;
    SubscriptionPlanDTO updateSubscriptionPlan(Long planId,SubscriptionPlanDTO planDTO) throws Exception;
    void deleteSubscriptionPlan(Long planId) throws Exception;
    List<SubscriptionPlanDTO> getAllSubscriptionPlans();
    SubscriptionPlan getBySubscriptionPlanCode(String subscriptionPlanCode) throws Exception;
}
