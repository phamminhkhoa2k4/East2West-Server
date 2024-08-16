package com.east2west.repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.east2west.models.Entity.Payment;
public interface PaymentRepository extends JpaRepository<Payment,Integer>{


}
