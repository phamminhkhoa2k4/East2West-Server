package com.east2west.models.mapper;

import com.east2west.models.DTO.CarDTO;
import com.east2west.models.Entity.Car;
import com.east2west.models.Entity.CarAvailability;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;


import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@Mapper
public interface CarMapper {

    CarMapper INSTANCE = Mappers.getMapper(CarMapper.class);

    @Mapping(source = "ward.wardname", target = "wardName")
    @Mapping(source = "ward.district.districtname", target = "districtName")
    @Mapping(source = "ward.district.cityprovince.cityname", target = "cityProvinceName")
    @Mapping(source = "enginesystem", target = "engineSystem")
    @Mapping(source = "locationtype", target = "locationType")
    @Mapping(source = "model.make", target = "make")
    @Mapping(source = "deposit", target = "deposit")
    @Mapping(source = "cancelFree", target = "cancelFree")
    @Mapping(source = "carAvailabilityList", target = "pricePerDay", qualifiedByName = "pricePerDay")
    @Mapping(source = "carAvailabilityList", target = "availableVehicles", qualifiedByName = "availableVehicles")
    CarDTO toDTO(Car car);

    @Mapping(target = "ward.wardname", source = "wardName")
    @Mapping(target = "ward.district.districtname", source = "districtName")
    @Mapping(target = "ward.district.cityprovince.cityname", source = "cityProvinceName")
    @Mapping(target = "enginesystem", source = "engineSystem")
    @Mapping(target = "locationtype", source = "locationType")
    Car toEntity(CarDTO carDTO);

    @Named("pricePerDay")
    static Double pricePerDay(List<CarAvailability> carAvailabilityList) {
        if (carAvailabilityList == null) return null;

        LocalDate today = LocalDate.now();

        Optional<CarAvailability> match = carAvailabilityList.stream()
                .filter(availability -> {
                    Timestamp timestamp = availability.getAvailabilityDateTime();
                    if (timestamp == null) return false;
                    LocalDate availabilityDate = timestamp.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    return availabilityDate.equals(today);
                })
                .findFirst();

        return match.map(a -> a.getPricePerDay().doubleValue()).orElse(null);
    }

    @Named("availableVehicles")
    static Integer availableVehicles(List<CarAvailability> carAvailabilityList) {
        if (carAvailabilityList == null) return null;

        LocalDate today = LocalDate.now();

        Optional<CarAvailability> match = carAvailabilityList.stream()
                .filter(availability -> {
                    Timestamp timestamp = availability.getAvailabilityDateTime();
                    if (timestamp == null) return false;
                    LocalDate availabilityDate = timestamp.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    return availabilityDate.equals(today);
                })
                .findFirst();

        return match.map(CarAvailability::getAvailableQuantity).orElse(0);
    }
}
