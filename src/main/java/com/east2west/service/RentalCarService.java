package com.east2west.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.east2west.repository.*;
import com.east2west.models.DTO.RentalDTO;
import com.east2west.models.Entity.Car;
import com.east2west.models.Entity.Payment;
import com.east2west.models.Entity.Rental;


import java.util.List;

@Service
public class RentalCarService {
    @Autowired
    private RentalRepository rentalCarRepository;

    public List<Rental> getAllBookings() {
        return rentalCarRepository.findAll();
    }
    // @Autowired
    // private UserRepository userRepository;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private PaymentRepository paymentRepository;
    // public Rental getBookingById(Long id) {
    //     return rentalCarRepository.findById(id).orElse(null);
    // }

    public Rental saveBooking(Rental bookingCar) {
        return rentalCarRepository.save(bookingCar);
    }
    public Rental saveRental(RentalDTO rentalDTO) {
        Car car = carRepository.findById(rentalDTO.getCarId())
            .orElseThrow(() -> new IllegalArgumentException("Car not found"));
        Payment payment = paymentRepository.findById(rentalDTO.getPaymentId())
            .orElseThrow(() -> new IllegalArgumentException("Payment not found"));

        Rental rental = new Rental();
        rental.setUserid(rentalDTO.getUserId());
        rental.setCar(car);
        rental.setPayment(payment);
        rental.setRentalDate(rentalDTO.getRentalDate());
        rental.setReturnDate(rentalDTO.getReturnDate());
        rental.setTotalAmount(rentalDTO.getTotalAmount());

        return rentalCarRepository.save(rental);
    }
    public List<Rental> getRentalsByUserId(int userId) {
        return rentalCarRepository.findByUserid(userId);
    }
}
