package com.east2west.repository;


import com.east2west.models.Entity.ActivityOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityOrderRepository extends JpaRepository<ActivityOrder, Long> {
}
