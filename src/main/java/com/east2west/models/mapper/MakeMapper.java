package com.east2west.models.mapper;


import com.east2west.models.DTO.MakeDTO;
import com.east2west.models.Entity.Make;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MakeMapper {
    MakeMapper INSTANCE = Mappers.getMapper(MakeMapper.class);

    MakeDTO toDTO(Make make);

    Make toEntity(MakeDTO makeDTO);
}
