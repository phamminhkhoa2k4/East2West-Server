package com.east2west.models.mapper;

import com.east2west.models.DTO.TypeDTO;
import com.east2west.models.Entity.Type;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;


@Mapper
public interface TypeMapper {

    TypeMapper INSTANCE = Mappers.getMapper(TypeMapper.class);

    TypeDTO toDTO(Type type);

    Type toEntity(TypeDTO typeDTO);
}
