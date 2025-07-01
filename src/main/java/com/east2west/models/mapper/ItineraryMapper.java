package com.east2west.models.mapper;


import com.east2west.models.DTO.ItineraryDTO;
import com.east2west.models.Entity.Itinerary;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ItineraryMapper {

    ItineraryMapper INSTANCE = Mappers.getMapper(ItineraryMapper.class);

    ItineraryDTO toDTO(Itinerary itinerary);

    Itinerary toEntity(ItineraryDTO itineraryDTO);
}
