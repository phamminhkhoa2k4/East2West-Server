package com.east2west.models.mapper;


import com.east2west.models.DTO.CarDTO;
import com.east2west.models.Entity.Car;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CarMapper {

    CarMapper INSTANCE = Mappers.getMapper(CarMapper.class);


    @Mapping(source = "ward.wardname", target = "wardName")
    @Mapping(source = "ward.district.districtname", target = "districtName")
    @Mapping(source = "ward.district.cityprovince.cityname", target = "cityProvinceName")
    @Mapping(source = "enginesystem", target = "engineSystem")
    @Mapping(source = "locationtype", target = "locationType")
    @Mapping(source = "model.make", target = "make")
    CarDTO toDTO(Car car);

    @Mapping(target = "ward.wardname", source = "wardName")
    @Mapping(target = "ward.district.districtname", source = "districtName")
    @Mapping(target = "ward.district.cityprovince.cityname", source = "cityProvinceName")
    @Mapping(target = "enginesystem", source = "engineSystem")
    @Mapping(target = "locationtype", source = "locationType")
    Car toEntity(CarDTO carDTO);
}



