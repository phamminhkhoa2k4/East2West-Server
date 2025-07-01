package com.east2west.models.DTO;

import jakarta.validation.constraints.Null;
import lombok.*;


import java.math.BigDecimal;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class  HomestayDTO {
    @Null
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
    private Integer room;
    private Boolean instant;
    private Integer bathroom;
    private Integer beds;
    private BigDecimal pricePerNight;
    private List<Integer> perkIds;
    private List<HomestayAvailabilityDTO> availability;


}