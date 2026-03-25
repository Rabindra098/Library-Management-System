package com.rb.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rb.model.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long>{

}
