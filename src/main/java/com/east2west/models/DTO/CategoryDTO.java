package com.east2west.models.DTO;


import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryDTO {
    private int categoryId;
    private String categoryName;

}