package com.east2west.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.east2west.models.Entity.Type;
public interface TypeRepository  extends JpaRepository<Type, Integer>{
    Optional<Type> findByTypeName(String typeName);
}
