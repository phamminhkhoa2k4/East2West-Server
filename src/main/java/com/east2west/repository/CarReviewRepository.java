package com.east2west.repository;


import com.east2west.models.DTO.CarReviewAverageDTO;
import com.east2west.models.Entity.CarReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarReviewRepository extends JpaRepository<CarReview,Integer> {

    @Query("SELECT AVG((r.worthTheMoney + r.cleanliness + r.rentalCounter + r.enthusiasticSupport + r.dropOffSpeed  + r.vehicleCondition + r.pickUpSpeed)/7) " +
            "FROM CarReview r WHERE r.car.carid = :carId")
    Double getAverageRatingByCar(int carId);

    List<CarReview> findByCar_Carid(int carId);

    @Query("SELECT new com.east2west.models.DTO.CarReviewAverageDTO(" +
            "AVG(cr.worthTheMoney), " +
            "AVG(cr.cleanliness), " +
            "AVG(cr.rentalCounter), " +
            "AVG(cr.enthusiasticSupport), " +
            "AVG(cr.dropOffSpeed), " +
            "AVG(cr.vehicleCondition), " +
            "AVG(cr.pickUpSpeed)) " +
            "FROM CarReview cr WHERE cr.car.carid = :carId")
    CarReviewAverageDTO getAverageDetailsByCar(int carId);


}
