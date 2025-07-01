package com.east2west.models.DTO;


import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ThemeDTO {
    private int themeId;
    private String themeName;
}