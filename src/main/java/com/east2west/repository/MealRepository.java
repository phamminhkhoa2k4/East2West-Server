package com.east2west.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.east2west.models.Entity.Meal;



public interface MealRepository extends JpaRepository<Meal, Integer> {

    Optional<Meal> findByMealname(String mealName);

    boolean existsByMealname(String mealname);

    List<Meal> findByMealnameContainingIgnoreCase(String name);

}
