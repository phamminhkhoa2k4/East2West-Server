package com.east2west.models.mapper;


import com.east2west.models.DTO.AccommodationDTO;
import com.east2west.models.Entity.Accommodation;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AccommodationMapper {

    AccommodationMapper INSTANCE = Mappers.getMapper(AccommodationMapper.class);

    AccommodationDTO toDTO(Accommodation accommodation);

    Accommodation toEntity(AccommodationDTO accommodationDTO);
}
