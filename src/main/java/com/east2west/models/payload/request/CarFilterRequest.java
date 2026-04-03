package com.east2west.models.payload.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarFilterRequest {
    private List<String> gearbox;
    private String sort;
    private List<String> type;
    private List<String> information;
    private List<String> engine;
    private List<String> mileage;
    private List<String> make;
    private List<String> location;
    private String fuelPolicy;
    private List<String> rating;
    private List<String> deposit;

    @NotNull
    private Double latitude;

    @NotNull
    private Double longitude;

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate pickupDate;

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dropOffDate;

    @NotNull
    @Positive
    private Double radius;
}
