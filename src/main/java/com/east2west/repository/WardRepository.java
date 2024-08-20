package com.east2west.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.east2west.models.Entity.Ward;

public interface WardRepository extends JpaRepository<Ward,Integer>{

    
}
