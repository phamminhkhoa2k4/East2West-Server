package com.east2west.models.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.math.BigDecimal;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingTourDTO {
    private int userId;
    private int paymentId;
    private int packageId;
    private Date tourDate;
    private int numberOfPeople;
    private BigDecimal totalPrice;
    private BigDecimal depositAmount;


    public String toParamString() {
        return String.format(
            "Tour Package ID: %d, User ID: %d, Tour Date: %s, Number of People: %d, Total Price: %s",
            packageId, userId, tourDate.toLocalDate().toString(), numberOfPeople, totalPrice.toString()
        );
    }
}
