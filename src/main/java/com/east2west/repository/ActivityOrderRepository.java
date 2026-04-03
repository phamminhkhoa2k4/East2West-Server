package com.east2west.repository;


import com.east2west.models.Entity.ActivityOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityOrderRepository extends JpaRepository<ActivityOrder, Long> {
}
