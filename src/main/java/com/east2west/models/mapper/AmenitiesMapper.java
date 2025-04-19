package com.east2west.models.mapper;

import com.east2west.models.DTO.AmenitiesDTO;
import com.east2west.models.Entity.Amenities;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AmenitiesMapper {
    AmenitiesMapper INSTANCE = Mappers.getMapper(AmenitiesMapper.class);

    AmenitiesDTO toDTO(Amenities amenities);

    Amenities toEntity(AmenitiesDTO amenitiesDTO);
}
