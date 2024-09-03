package com.east2west.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.east2west.models.Entity.Meal;



public interface MealRepository extends JpaRepository<Meal, Integer> {
    List<Meal> findByMealnameContainingIgnoreCase(String name);
}
