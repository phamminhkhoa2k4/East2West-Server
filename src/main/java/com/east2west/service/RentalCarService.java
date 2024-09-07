package com.east2west.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.east2west.repository.*;
import com.east2west.exception.ResourceNotFoundException;
import com.east2west.models.DTO.CancelDTO;
import com.east2west.models.DTO.RentalDTO;
import com.east2west.models.DTO.RentalFetch;
import com.east2west.models.DTO.UserFetch;
import com.east2west.models.Entity.Car;
import com.east2west.models.Entity.Payment;
import com.east2west.models.Entity.Rental;
import com.east2west.models.Entity.User;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RentalCarService {
    @Autowired
    private RentalRepository rentalCarRepository;
@Autowired
private UserRepository userRepository; 
    public List<RentalFetch> getAllRentals() {
    List<Rental> rentals = rentalCarRepository.findAll();
    return rentals.stream().map(rental -> {
        // Truy xuất thông tin người dùng bằng userId
        User user = userRepository.findById(rental.getUserid()).orElse(null);

        UserFetch userFetch = new UserFetch();
        if (user != null) {
            userFetch.setFirstname(user.getFirstname());
            userFetch.setLastname(user.getLastname());
            userFetch.setPhone(user.getPhone());
        }

        RentalFetch rentalFetch = new RentalFetch();
        rentalFetch.setRentalId(rental.getRentalid());
        rentalFetch.setCarName(rental.getCar().getCarName()); // Tên xe
        rentalFetch.setUser(userFetch);
        rentalFetch.setStatus(rental.getStatus());
        rentalFetch.setRentalDate(rental.getRentalDate());
        rentalFetch.setTotalAmount(rental.getTotalAmount());
        return rentalFetch;
    }).collect(Collectors.toList());
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
        rental.setStatus("Waiting");
        rental.setRentalDate(rentalDTO.getRentalDate());
        rental.setReturnDate(rentalDTO.getReturnDate());
        rental.setTotalAmount(rentalDTO.getTotalAmount());
        return rentalCarRepository.save(rental);
    }
    public List<Rental> getRentalsByUserId(int userId) {
        return rentalCarRepository.findByUserid(userId);
    }
    public Rental findById(int rentalid) {
        Optional<Rental> rental = rentalCarRepository.findById(rentalid);
        return rental.orElse(null);
    }

}
