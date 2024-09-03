package com.east2west.models.DTO;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;


@Getter
@Setter
public class HomestayAvailabilityDTO {
    private int homestayavailabilityid;
    private Timestamp date;
    private BigDecimal pricepernight;
    private String status;
}
