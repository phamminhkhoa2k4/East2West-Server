package com.east2west.models.DTO;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class TourPackageDTO {
    private int packageid;
    private String title;
    private List<String> thumbnail;
    private BigDecimal price;
    private String groupsize;
    private String deposit;
    private Set<CategoryDTO> categories;
    private Set<ThemeDTO> themes;
    private Set<SuitableDTO> suitable;
    private List<DepartureDateDTO> departuredates;
    private List<ItineraryDTO> itineraries;


}