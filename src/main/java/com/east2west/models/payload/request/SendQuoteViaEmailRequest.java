package com.east2west.models.payload.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SendQuoteViaEmailRequest {

    @NotBlank
    @Email
    private String email;

    @NotNull
    private Integer carId;

    @NotNull
    private LocalDate dropOffDate;

    @NotNull
    private LocalDate pickUpDate;

    @NotBlank
    private String pickUpTime;

    @NotBlank
    private String dropOffTime;


}
