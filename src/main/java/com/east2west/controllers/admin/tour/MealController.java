package com.east2west.controllers.admin.tour;


import com.east2west.models.DTO.MealDTO;
import com.east2west.models.DTO.ModelResponse;
import com.east2west.models.Entity.Meal;
import com.east2west.service.MealService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tours/meals")
public class MealController {

    private  final MealService mealService;

    public MealController(MealService mealService) {
        this.mealService = mealService;
    }

    // Endpoint: Create Meal
    @PostMapping
    public ResponseEntity<ModelResponse<MealDTO>> createMeal(@RequestBody MealDTO meal) {
        try {
            Optional<Meal> meals = mealService.findByMealName(meal.getMealname());

            if(meals.isPresent()){
                return ResponseEntity.status(HttpStatus.OK).body(
                        ModelResponse.<MealDTO>builder()
                                .status(400)
                                .message("Meal name " + meal.getMealname() + " already exists.")
                                .data(null)
                                .build()
                );
            }
            MealDTO data = mealService.createMeal(meal);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    ModelResponse.<MealDTO>builder()
                            .status(201)
                            .message(data.getMealname() + " meal created successfully !!!")
                            .data(data)
                            .build()
            );
        }catch (Exception ex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    ModelResponse.<MealDTO>builder()
                            .status(400)
                            .message("Error Internal Server !!!")
                            .data(null)
                            .build()
            );
        }
    }


    // Endpoint: Delete meal by id
    @DeleteMapping("/{id}")
    public ResponseEntity<ModelResponse<Meal>> deleteMeal(@PathVariable int id) {
        try {
            String data = mealService.deleteMeal(id);
            if(data.startsWith("Deleted")){
                return ResponseEntity.status(HttpStatus.OK).body(
                        ModelResponse.<Meal>builder()
                                .status(200)
                                .message(data)
                                .data(null)
                                .build()
                );
            }else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        ModelResponse.<Meal>builder()
                                .status(404)
                                .message(data)
                                .data(null)
                                .build()
                );
            }

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ModelResponse.<Meal>builder()
                            .status(500)
                            .message("INTERNAL SERVER ERROR !!!")
                            .data(null)
                            .build()
            );

        }
    }

    // Endpoint: Pagination
    @GetMapping
    public Page<MealDTO> getMeals(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return mealService.getAllMeals(PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "mealid")));
    }

    // Endpoint: Search meal
    @GetMapping("/search")
    public List<MealDTO> searchMeal(@RequestParam String keyword) {
        return mealService.searchMeal(keyword);
    }


    // Endpoint: Get a meal by id
    @GetMapping("/{id}")
    public ResponseEntity<ModelResponse<Optional<MealDTO>>> getMakeById(@PathVariable int id){
        try {
            Optional<MealDTO> data = mealService.getMealById(id);
            if(data.isPresent()){
                return ResponseEntity.status(HttpStatus.OK).body(
                        ModelResponse.<Optional<MealDTO>>builder()
                                .status(200)
                                .message("OK")
                                .data(data)
                                .build()
                );
            }else{
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        ModelResponse.<Optional<MealDTO>>builder()
                                .status(404)
                                .message("Not found make !!!")
                                .data(null)
                                .build()
                );
            }
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ModelResponse.<Optional<MealDTO>>builder()
                            .status(500)
                            .message("INTERNAL SERVER ERROR")
                            .data(null)
                            .build()
            );
        }

    }



    // Endpoint: Update meal
    @PutMapping
    public ResponseEntity<ModelResponse<MealDTO>> updateTransfer(@RequestBody MealDTO meal) {
        try {
            MealDTO data = mealService.updateMeal(meal);
            if(data != null){
                return ResponseEntity.status(HttpStatus.OK).body(
                        ModelResponse.<MealDTO>builder()
                                .status(200)
                                .message(data.getMealname() +" meal updated successfully !!!")
                                .data(data)
                                .build()
                );
            }else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        ModelResponse.<MealDTO>builder()
                                .status(404)
                                .message("Meal not found !!!")
                                .data(null)
                                .build()
                );
            }

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ModelResponse.<MealDTO>builder()
                            .status(500)
                            .message("INTERNAL SERVER ERROR")
                            .data(null)
                            .build()
            );

        }
    }


    // Endpoint: Create meal  by file csv
    @PostMapping("/uploadfromcsv")
    public ResponseEntity<ModelResponse<?>> uploadFile(@RequestParam("file") MultipartFile[] file) {
        try {

            String data = mealService.saveMealFromCSV(file);

            return ResponseEntity.status(HttpStatus.CREATED).body(
                    ModelResponse.<MealDTO>builder()
                            .status(201)
                            .message("Create successfully " + data +" meal !!!").data(null).build());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ModelResponse.<MealDTO>builder()
                            .status(500)
                            .message(String.valueOf(e)).data(null).build());
        }
    }


    // Endpoint: List meal
    @GetMapping("/list")
    public ResponseEntity<ModelResponse<List<MealDTO>>> listMeal(){
        try{
            List<MealDTO> data = mealService.getAllMeal();
            return ResponseEntity.status(HttpStatus.OK).body(
                    ModelResponse.<List< MealDTO>>builder().status(200).message("OK").data(data).build()
            );
        }catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ModelResponse.<List<MealDTO>>builder().status(200).message("INTERNAL SERVER ERROR").data(null).build()
            );
        }
    }



}
