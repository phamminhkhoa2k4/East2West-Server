package com.east2west.models.DTO;


import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PhotoDeleteDTO {
    private String url;
    private int id;


}
