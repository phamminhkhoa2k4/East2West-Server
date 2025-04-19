package com.east2west.models.mapper;

import com.east2west.models.DTO.StructureDTO;
import com.east2west.models.Entity.Structure;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface StructureMapper {
    StructureMapper INSTANCE = Mappers.getMapper(StructureMapper.class);

    StructureDTO toDTO(Structure structure);

    Structure toEntity(StructureDTO structureDTO);
}
