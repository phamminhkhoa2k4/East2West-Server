package com.east2west.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.east2west.models.Entity.BookingHomestay;

import java.util.List;


public interface BookingHomestayRepository extends JpaRepository<BookingHomestay, Integer>{

    List<BookingHomestay> findByStatus(String status);

    List<BookingHomestay> findByStatusAndUserUserId(String status, int userId);
}
