package com.east2west.models.DTO;

import com.east2west.models.Entity.Make;
import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ModelDTO {
    private int modelid;

    private String modelname;

    private Make make;

}
