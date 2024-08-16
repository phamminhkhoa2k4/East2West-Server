package com.east2west.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.east2west.models.Entity.Model;

public interface ModelRepository extends JpaRepository<Model, Integer>{
    Optional<Model> findByModelName(String modelName);

}
