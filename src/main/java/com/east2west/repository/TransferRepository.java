package com.east2west.repository;

import com.east2west.models.Entity.Meal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import com.east2west.models.Entity.Transfer;

@Repository
public interface TransferRepository extends JpaRepository<Transfer, Integer> {

    List<Transfer> findByTransfernameContainingIgnoreCase(String name);

    Optional<Transfer> findByTransfername(String name);

    boolean existsByTransfername(String transfername);
}
