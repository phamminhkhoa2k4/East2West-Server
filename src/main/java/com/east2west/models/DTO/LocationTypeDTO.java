package com.east2west.models.DTO;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class LocationTypeDTO {
    private int locationtypeid;

    private String locationtypename;

    private String locationtypedescription;
}
