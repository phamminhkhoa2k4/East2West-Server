package com.east2west.models.DTO;

import com.east2west.models.Entity.Make;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ModelDTO {
    private int modelid;

    private String modelname;

    private Make make;

}
