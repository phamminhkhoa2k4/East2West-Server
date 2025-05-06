package com.east2west.models.DTO;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class ThemeDTO {
    private int themeId;
    private String themeName;
}