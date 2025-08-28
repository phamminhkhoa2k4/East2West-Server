package com.east2west.models.mapper;


import com.east2west.models.DTO.HomestayAvailabilityDTO;
import com.east2west.models.DTO.HomestayDTO;
import com.east2west.models.Entity.Amenities;
import com.east2west.models.Entity.Homestay;
import com.east2west.models.Entity.HomestayAvailability;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Mapper
public interface HomestayMapper {

    HomestayMapper INSTANCE = Mappers.getMapper(HomestayMapper.class);

    @Mapping(source = "ward.wardname", target = "wardName")
    @Mapping(source = "ward.district.districtname", target = "districtName")
    @Mapping(source = "ward.district.cityprovince.cityname", target = "cityProvinceName")
    @Mapping(source = "structure.structureid", target = "structureId")
    @Mapping(source = "userid", target = "userId")
    @Mapping(source = "maxguest", target = "maxGuest")
    @Mapping(source = "extrainfo", target = "extraInfo")
    @Mapping(source = "cleaningfee", target = "cleaningFee")
    @Mapping(source = "amenities", target = "amenityIds", qualifiedByName = "mapAmenityListToIdList")
    @Mapping(source = "homestayAvailabilityList", target = "availability", qualifiedByName = "mapAvailabilityList")
    @Mapping(source = "homestayAvailabilityList", target = "pricePerNight", qualifiedByName = "pricePerDay")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "createdAt", target = "createdAt")
    @Mapping(source = "updatedAt", target = "updatedAt")


    HomestayDTO toDTO(Homestay homestay);

    @Mapping(target = "ward.wardname", source = "wardName")
    @Mapping(target = "ward.district.districtname", source = "districtName")
    @Mapping(target = "ward.district.cityprovince.cityname", source = "cityProvinceName")
//    @Mapping(target = "structure.structureid", source = "structureId")

    Homestay toEntity(HomestayDTO departureDateDTO);

    @Named("mapAmenityListToIdList")
    static List<Integer> mapAmenityListToIdList(List<Amenities> amenities) {
        if (amenities == null) return null;
        return amenities.stream()
                .map(Amenities::getAmenitiesid)
                .collect(Collectors.toList());
    }

    @Named("pricePerDay")
    static BigDecimal pricePerDay(List<HomestayAvailability> homestayAvailabilityList) {
        if (homestayAvailabilityList == null) return null;

        LocalDate today = LocalDate.now();

        Optional<HomestayAvailability> match = homestayAvailabilityList.stream()
                .filter(availability -> {
                    Timestamp timestamp = availability.getDate();
                    if (timestamp == null) return false;
                    LocalDate availabilityDate = timestamp.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    return availabilityDate.equals(today);
                })
                .findFirst();

        return match.map(HomestayAvailability::getPricepernight).orElse(null);
    }


    @Named("mapAvailabilityList")
    default List<HomestayAvailabilityDTO> mapAvailabilityList(List<HomestayAvailability> availabilities) {
        if (availabilities == null) return null;
        return availabilities.stream()
                .map(this::toAvailabilityDTO)
                .toList();
    }

    @Mapping(source = "homestay.homestayid", target = "homestayid")
    HomestayAvailabilityDTO toAvailabilityDTO(HomestayAvailability availability);

}
