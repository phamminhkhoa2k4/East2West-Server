package com.east2west.models.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarReviewAverageDTO {
    @Builder.Default
    private Double worthTheMoney = 0.0;
    @Builder.Default
    private Double cleanliness = 0.0;
    @Builder.Default
    private Double rentalCounter = 0.0;
    @Builder.Default
    private Double enthusiasticSupport = 0.0;
    @Builder.Default
    private Double dropOffSpeed  = 0.0;
    @Builder.Default
    private Double vehicleCondition = 0.0;
    @Builder.Default
    private Double pickUpSpeed = 0.0;
}