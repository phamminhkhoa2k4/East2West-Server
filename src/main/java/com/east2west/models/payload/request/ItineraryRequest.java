package com.east2west.models.payload.request;


import lombok.*;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItineraryRequest {

    private int day;


    private List<ActivityOrderRequest> activityOrders;
}
