package com.east2west.models.DTO;

import lombok.*;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItineraryDTO {

    private int itineraryId;
    private int tourPackageId;
    private int day;
    private List<ActivityOrderDTO> activityOrders;
}

