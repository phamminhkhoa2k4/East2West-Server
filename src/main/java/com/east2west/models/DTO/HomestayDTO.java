package com.east2west.models.DTO;

import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class  HomestayDTO {
    private Integer homestayid;
    private Integer structureId;
    private Integer userId;
    private  String cityProvinceName;
    private  String districtName;
    private String wardName;
    private Double longitude;
    private Double latitude;
    private String title;
    private String address;
    private String geom;
    private List<String> photos;
    private String description;
    private String extraInfo;
    private BigDecimal cleaningFee;
    private Boolean isApproved;
    private String type;
    private Integer maxGuest;
    private BigDecimal pricePerNight;
    private List<Integer> perkIds;
    private List<HomestayAvailabilityDTO> availability;


}