package com.east2west.models.DTO;
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































}
