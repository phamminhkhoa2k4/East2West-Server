package com.east2west.models.DTO;
import java.math.BigDecimal;
import java.util.List;
import com.east2west.models.Entity.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarDTO {
    private int carid;

    private String carName;

    private Double pricePerDay;

    private  Integer quantity;

    private Integer availableVehicles;

    private String status;

    private Type type;

    private Make make;

    private Model model;

    private Integer year;

    private Integer seatCapacity;

    private String fueltankcapacity;

    private String engineSystem;

    private String fuel;

    private String miles;

    private String cargearbox;

    private LocationType locationType;

    private boolean airConditioned;

    private boolean fourDoorsOrMore;

    private double latitude;

    private double longitude;

    private  String cityProvinceName;

    private  String districtName;

    private String wardName;

    private String location;

    private List<String> thumbnail;

    private Integer largeLuggage;

    private Integer smallLuggage;

    private Boolean fuelSameReturn;

    @Builder.Default
    private Double averageRating = 0.0;

    @Builder.Default
    private Integer numberOfReviews = 1;

    private CarReviewAverageDTO reviewAverage;

    private BigDecimal deposit;

    private Boolean cancelFree;

}
