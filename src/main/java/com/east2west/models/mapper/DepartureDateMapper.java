package com.east2west.models.mapper;

import com.east2west.models.DTO.DepartureDateDTO;
import com.east2west.models.Entity.DepartureDate;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface DepartureDateMapper {
    DepartureDateMapper INSTANCE = Mappers.getMapper(DepartureDateMapper.class);

    DepartureDateDTO toDTO(DepartureDate departureDate);

    DepartureDate toEntity(DepartureDateDTO departureDateDTO);
}
