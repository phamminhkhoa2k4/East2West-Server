package com.east2west.models.mapper;

import com.east2west.models.DTO.CarDTO;
import com.east2west.models.DTO.MealDTO;
import com.east2west.models.Entity.Car;
import com.east2west.models.Entity.Meal;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MealMapper {

    MealMapper INSTANCE = Mappers.getMapper(MealMapper.class);

    MealDTO toDTO(Meal meal);

    Meal toEntity(MealDTO mealDTO);

}


