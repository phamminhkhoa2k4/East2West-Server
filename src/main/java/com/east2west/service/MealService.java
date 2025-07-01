package com.east2west.service;

import com.east2west.models.DTO.MealDTO;
import com.east2west.models.Entity.Make;
import com.east2west.models.Entity.Meal;
import com.east2west.models.mapper.MealMapper;
import com.east2west.repository.MealRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class MealService {

    private final MealRepository mealRepository;


    public MealService(MealRepository mealRepository) {
        this.mealRepository = mealRepository;
    }


    public Optional<Meal> findByMealName(String mealName){
        return mealRepository.findByMealname(mealName);
    }

    public MealDTO createMeal(MealDTO meal) {
        return MealMapper.INSTANCE.toDTO(mealRepository.save(
                Meal.builder()
                        .mealid(meal.getMealid())
                        .mealname(meal.getMealname())
                        .mealduration(meal.getMealduration())
                        .mealactivity(meal.getMealactivity())
                        .mealthumbnail(meal.getMealthumbnail())
                        .build()
        ));
    }


    public String deleteMeal(int id){

        Optional<Meal> meals = mealRepository.findById(id);
        if(meals.isPresent()){
            mealRepository.deleteById(id);
            return "Deleted " + meals.get().getMealname() + " meal successfully";
        }else{
            return "Not found meal";
        }
    }

    public Page<MealDTO> getAllMeals(Pageable pageable) {
        Page<Meal> mealPage =mealRepository.findAll(pageable);
        return mealPage.map(MealMapper.INSTANCE::toDTO);
    }

    public List<MealDTO> searchMeal(String keyword) {
        List<Meal> mealList = mealRepository.findByMealnameContainingIgnoreCase(keyword);
        return mealList.stream().map(MealMapper.INSTANCE::toDTO).toList();
    }


    public Optional<MealDTO> getMealById(int id) {
        Optional<Meal> meal = mealRepository.findById(id);
        if (meal.isPresent()){
            MealDTO mealDTO  = MealMapper.INSTANCE.toDTO(meal.get());
            return Optional.ofNullable(mealDTO);
        }
        return Optional.empty();
    }


    public MealDTO updateMeal(MealDTO meal){
        Optional<Meal> meals =  mealRepository.findById(meal.getMealid());
        if(meals.isPresent()){
            Meal data = MealMapper.INSTANCE.toEntity(meal);
            return MealMapper.INSTANCE.toDTO(mealRepository.save(data));
        }
        return null;
    }



    public String saveMealFromCSV(@NotNull MultipartFile[] files) {
        if (files.length == 0) {
            return "Chưa chọn file!";
        }

        try {
            List<Meal> mealsList = new ArrayList<>();
            Optional<Meal> maxIdMeal = mealRepository.findAll().stream()
                    .max(Comparator.comparingInt(Meal::getMealid));
            int idCounter = maxIdMeal.map(meal -> meal.getMealid() + 1)
                    .orElse(1);

            for (MultipartFile file : files) {
                if (file.isEmpty()) {
                    continue;
                }

                BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));
                String line;
                boolean isFirstLine = true;

                while ((line = reader.readLine()) != null) {
                    if (isFirstLine) {
                        isFirstLine = false;
                        continue;
                    }

                    String[] data = line.split(",");
                    if (data.length < 3) {
                        continue;
                    }



                    String MealThumbnail = data[0].trim();
                    String MealName = data[1].trim();
                    String MealDuration = data[2].trim();
                    System.out.println("Đang xử lý tiện ích: " + MealName);

                    Meal meal = new Meal();
                    meal.setMealid(idCounter++);
                    meal.setMealthumbnail(MealThumbnail);
                    meal.setMealname(MealName);
                    meal.setMealduration(MealDuration);
                    mealsList.add(meal);
                }
            }


            mealRepository.saveAll(mealsList);
            return "" + mealsList.size();
        } catch (Exception e) {
            return "Lỗi xử lý file: " + e.getMessage();
        }
    }

    public List<MealDTO> getAllMeal(){
        return mealRepository.findAll().stream().map(MealMapper.INSTANCE::toDTO).toList();
    }
}
