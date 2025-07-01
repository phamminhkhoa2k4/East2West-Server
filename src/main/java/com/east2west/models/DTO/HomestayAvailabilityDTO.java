package com.east2west.models.DTO;

import lombok.*;

import java.math.BigDecimal;
import java.sql.Timestamp;



@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HomestayAvailabilityDTO {
    private int homestayavailabilityid;
    private Timestamp date;
    private BigDecimal pricepernight;
    private String status;
    private int homestayid;
}
