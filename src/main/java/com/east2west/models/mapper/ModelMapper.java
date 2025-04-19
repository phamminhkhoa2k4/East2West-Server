package com.east2west.models.mapper;


import com.east2west.models.DTO.ModelDTO;
import com.east2west.models.Entity.Model;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ModelMapper {

    ModelMapper INSTANCE = Mappers.getMapper(ModelMapper.class);

    ModelDTO toDTO(Model model);

    Model toEntity(ModelDTO modelDTO);
}
