package com.east2west.repository;


import com.east2west.models.DTO.CarReviewAverageDTO;
import com.east2west.models.Entity.CarReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarReviewRepository extends JpaRepository<CarReview,Integer> {

    @Query("SELECT (" +
            "COALESCE(AVG(r.worthTheMoney), 0) + " +
            "COALESCE(AVG(r.cleanliness), 0) + " +
            "COALESCE(AVG(r.rentalCounter), 0) + " +
            "COALESCE(AVG(r.enthusiasticSupport), 0) + " +
            "COALESCE(AVG(r.dropOffSpeed), 0) + " +
            "COALESCE(AVG(r.vehicleCondition), 0) + " +
            "COALESCE(AVG(r.pickUpSpeed), 0)) " +
            "FROM CarReview r WHERE r.car.carid = :carId")
    Double getAverageRatingByCar(int carId);

    List<CarReview> findByCar_Carid(int carId);

    @Query("SELECT new com.east2west.models.DTO.CarReviewAverageDTO(" +
            "COALESCE(AVG(cr.worthTheMoney), 0), " +
            "COALESCE(AVG(cr.cleanliness), 0), " +
            "COALESCE(AVG(cr.rentalCounter), 0), " +
            "COALESCE(AVG(cr.enthusiasticSupport), 0), " +
            "COALESCE(AVG(cr.dropOffSpeed), 0), " +
            "COALESCE(AVG(cr.vehicleCondition), 0), " +
            "COALESCE(AVG(cr.pickUpSpeed), 0)) " +
            "FROM CarReview cr WHERE cr.car.carid = :carId")
    CarReviewAverageDTO getAverageDetailsByCar(int carId);


}
