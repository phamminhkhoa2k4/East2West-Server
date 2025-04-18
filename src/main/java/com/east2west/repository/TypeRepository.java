package com.east2west.repository;
import java.util.List;
import java.util.Optional;

import com.east2west.models.Entity.Make;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.east2west.models.Entity.Type;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeRepository  extends JpaRepository<Type, Integer>{
    Optional<Type> findByTypename(String typeName);
    Page<Type> findAll(Pageable pageable);
    List<Type> findByTypenameContainingIgnoreCase(String keyword);
}
