package com.east2west.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import com.east2west.models.Entity.Transfer;

@Repository
public interface TransferRepository extends JpaRepository<Transfer, Integer> {
    List<Transfer> findByTransfernameContainingIgnoreCase(String name);
    boolean existsByTransfername(String transfername);
}
