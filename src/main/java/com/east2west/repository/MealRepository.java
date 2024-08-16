package com.east2west.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.east2west.models.Entity.Meal;



public interface MealRepository extends JpaRepository<Meal, Integer> {
    
}
