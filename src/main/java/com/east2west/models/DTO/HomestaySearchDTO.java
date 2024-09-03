package com.east2west.models.DTO;

import com.east2west.constans.AvailabilityStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HomestaySearchDTO {
    private Double longitude;
    private Double latitude;
    private Double radius;
    private LocalDate checkinDate;
    private LocalDate checkoutDate;
    private Integer guests;
    private AvailabilityStatus status;
}








