package com.east2west.models.DTO;


import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StructureDTO {
    private int structureid;

    private String structurename;

    private String structureicon;
}
