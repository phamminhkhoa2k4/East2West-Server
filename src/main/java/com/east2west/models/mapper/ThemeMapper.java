package com.east2west.models.mapper;

import com.east2west.models.DTO.ThemeDTO;
import com.east2west.models.Entity.Theme;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ThemeMapper {
    ThemeMapper INSTANCE = Mappers.getMapper(ThemeMapper.class);

    ThemeDTO toDTO(Theme theme);

    Theme toEntity(ThemeDTO themeDTO);
}
