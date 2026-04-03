package com.east2west.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.east2west.models.Entity.Payment;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment,Integer>{


}
