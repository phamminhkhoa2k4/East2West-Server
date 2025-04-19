package com.east2west.models.mapper;

import com.east2west.models.DTO.TransferDTO;
import com.east2west.models.Entity.Transfer;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TransferMapper {

    TransferMapper INSTANCE = Mappers.getMapper(TransferMapper.class);

    TransferDTO toDTO(Transfer transfer);

    Transfer toEntity(TransferDTO transferDTO);
}
