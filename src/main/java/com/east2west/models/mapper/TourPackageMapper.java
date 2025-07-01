package com.east2west.models.mapper;


import com.east2west.models.DTO.TourPackageDTO;
import com.east2west.models.Entity.TourPackage;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {DepartureDateMapper.class})
public interface TourPackageMapper {
    TourPackageMapper INSTANCE = Mappers.getMapper(TourPackageMapper.class);

    TourPackageDTO toDTO(TourPackage tourPackage);

    TourPackage toEntity(TourPackageDTO tourPackageDTO);



}
