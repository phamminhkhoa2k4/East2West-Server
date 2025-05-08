package com.east2west.controllers.admin.tour;


import com.east2west.models.DTO.ModelResponse;
import com.east2west.models.Entity.Category;
import com.east2west.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/api/tours/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }


    // Endpoint: Create category
    @PostMapping
    public ResponseEntity<ModelResponse<CategoryDTO>> createCategory(@RequestBody CategoryDTO categoryDto) {
        try {
            Optional<Category> category = categoryService.findByCategoryName(categoryDto.getCategoryName());

            if(category.isPresent()){
                return ResponseEntity.status(HttpStatus.OK).body(
                        ModelResponse.<CategoryDTO>builder()
                                .status(400)
                                .message("Category name " + categoryDto.getCategoryName() + " already exists.")
                                .data(null)
                                .build()
                );
            }
            CategoryDTO data = categoryService.createCategory(categoryDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    ModelResponse.<CategoryDTO>builder()
                            .status(201)
                            .message(data.getCategoryName() + " category created successfully !!!")
                            .data(data)
                            .build()
            );
        }catch (Exception ex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    ModelResponse.<CategoryDTO>builder()
                            .status(400)
                            .message("Error Internal Server !!!")
                            .data(null)
                            .build()
            );
        }
    }


    // Endpoint: Delete category by id
    @DeleteMapping("/{id}")
    public ResponseEntity<ModelResponse<Category>> deleteCategory(@PathVariable int id) {
        try {
            String data = categoryService.deleteCategory(id);
            if(data.startsWith("Deleted")){
                return ResponseEntity.status(HttpStatus.OK).body(
                        ModelResponse.<Category>builder()
                                .status(200)
                                .message(data)
                                .data(null)
                                .build()
                );
            }else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        ModelResponse.<Category>builder()
                                .status(404)
                                .message(data)
                                .data(null)
                                .build()
                );
            }

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ModelResponse.<Category>builder()
                            .status(500)
                            .message("INTERNAL SERVER ERROR !!!")
                            .data(null)
                            .build()
            );

        }
    }

    // Endpoint: Pagination
    @GetMapping
    public Page<CategoryDTO> getCategoryList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return categoryService.getAllCategoryList(PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "categoryId")));
    }

    // Endpoint: Search category
    @GetMapping("/search")
    public List<CategoryDTO> searchCategory(@RequestParam String keyword) {
        return categoryService.searchCategory(keyword);
    }


    // Endpoint: Get a category by id
    @GetMapping("/{id}")
    public ResponseEntity<ModelResponse<Optional<CategoryDTO>>> getCategoryById(@PathVariable int id){
        try {
            Optional<CategoryDTO> data = categoryService.getCategoryById(id);
            if(data.isPresent()){
                return ResponseEntity.status(HttpStatus.OK).body(
                        ModelResponse.<Optional<CategoryDTO>>builder()
                                .status(200)
                                .message("OK")
                                .data(data)
                                .build()
                );
            }else{
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        ModelResponse.<Optional<CategoryDTO>>builder()
                                .status(404)
                                .message("Not found category !!!")
                                .data(null)
                                .build()
                );
            }
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ModelResponse.<Optional<CategoryDTO>>builder()
                            .status(500)
                            .message("INTERNAL SERVER ERROR")
                            .data(null)
                            .build()
            );
        }

    }



    // Endpoint: Update category
    @PutMapping
    public ResponseEntity<ModelResponse<CategoryDTO>> updateCategory(@RequestBody CategoryDTO categoryDto) {
        try {
            CategoryDTO data = categoryService.updateCategory(categoryDto);
            if(data != null){
                return ResponseEntity.status(HttpStatus.OK).body(
                        ModelResponse.<CategoryDTO>builder()
                                .status(200)
                                .message(data.getCategoryName() +" category updated successfully !!!")
                                .data(data)
                                .build()
                );
            }else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        ModelResponse.<CategoryDTO>builder()
                                .status(404)
                                .message("Category not found !!!")
                                .data(null)
                                .build()
                );
            }

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ModelResponse.<CategoryDTO>builder()
                            .status(500)
                            .message("INTERNAL SERVER ERROR")
                            .data(null)
                            .build()
            );

        }
    }


    // Endpoint: Create category  by file csv
    @PostMapping("/uploadfromcsv")
    public ResponseEntity<ModelResponse<?>> uploadFile(@RequestParam("file") MultipartFile[] file) {
        try {

            String data = categoryService.saveCategoryFromCSV(file);

            return ResponseEntity.status(HttpStatus.CREATED).body(
                    ModelResponse.<CategoryDTO>builder()
                            .status(201)
                            .message("Create successfully " + data +" category !!!").data(null).build());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ModelResponse.<CategoryDTO>builder()
                            .status(500)
                            .message(String.valueOf(e)).data(null).build());
        }
    }
}
