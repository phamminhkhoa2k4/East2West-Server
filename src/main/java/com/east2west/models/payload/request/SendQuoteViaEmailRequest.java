package com.east2west.models.payload.request;


import com.east2west.models.DTO.CarDTO;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SendQuoteViaEmailRequest {

    private String email;

    private Integer carId;

    private LocalDate dropOffDate;

    private LocalDate pickUpDate;

    private String pickUpTime;

    private String dropOffTime;


}
