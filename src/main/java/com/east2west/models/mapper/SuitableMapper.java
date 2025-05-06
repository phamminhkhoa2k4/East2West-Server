package com.east2west.models.mapper;


import com.east2west.models.DTO.SuitableDTO;
import com.east2west.models.Entity.Suitable;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SuitableMapper {

    SuitableMapper INSTANCE = Mappers.getMapper(SuitableMapper.class);

    SuitableDTO toDTO(Suitable suitable);

    Suitable toEntity(SuitableDTO suitableDTO);
}
