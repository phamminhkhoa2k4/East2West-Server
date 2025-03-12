package com.east2west.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.east2west.models.Entity.Make;

@Repository
public interface MakeRepository extends JpaRepository<Make, Integer>{
     Optional<Make> findByMakename(String makeName);

     Page<Make> findAll(Pageable pageable);

     List<Make> findByMakenameContainingIgnoreCase(String keyword);
}