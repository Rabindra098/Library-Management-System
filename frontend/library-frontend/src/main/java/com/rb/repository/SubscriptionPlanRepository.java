package com.rb.repository;

import com.rb.model.SubscriptionPlan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriptionPlanRepository
        extends JpaRepository<SubscriptionPlan, Long> {

    Boolean existsByPlanCode(String planCode);
    SubscriptionPlan findByPlanCode(String planCode);
}
