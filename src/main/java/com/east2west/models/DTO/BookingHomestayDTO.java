package com.east2west.models.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BookingHomestayDTO {
    private int homestayavailabilityId;
    private int userId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Timestamp checkin;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Timestamp bookingdate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Timestamp checkout;
    private BigDecimal feeamount;

    private String status;
    private int numberofguest;
    private BigDecimal totalPrice;

    public String toParamString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Homestay Availability ID: ").append(homestayavailabilityId).append(", ")
                .append("User ID: ").append(userId).append(", ")
                .append("Check-in: ").append(checkin).append(", ")
                .append("Check-out: ").append(checkout).append(", ")
                .append("Fee Amount: ").append(feeamount).append(", ")
                .append("Status: ").append(status).append(", ")
                .append("Number of Guests: ").append(numberofguest).append(", ")
                .append("Total Price: ").append(totalPrice);
        return sb.toString();
    }
    // Getters and Setters
}