package com.east2west.models.mapper;

import com.east2west.models.DTO.PlaceDTO;
import com.east2west.models.Entity.Place;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PlaceMapper {

    PlaceMapper INSTANCE = Mappers.getMapper(PlaceMapper.class);

    PlaceDTO toDTO(com.east2west.models.Entity.Place place);

    Place toEntity(com.east2west.models.DTO.PlaceDTO placeDTO);
}
