package com.east2west.models.mapper;

import com.east2west.models.DTO.LocationTypeDTO;
import com.east2west.models.Entity.LocationType;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface LocationTypeMapper {
    LocationTypeMapper INSTANCE = Mappers.getMapper(LocationTypeMapper.class);

    LocationTypeDTO toDTO(LocationType locationType);

    LocationType toEntity(LocationTypeDTO  locationTypeDTO);
}
