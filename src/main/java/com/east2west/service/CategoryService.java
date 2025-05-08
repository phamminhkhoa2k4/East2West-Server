package com.east2west.service;

import com.east2west.models.Entity.Category;
import com.east2west.models.mapper.CategoryMapper;
import com.east2west.repository.CategoryRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
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
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }


    public Optional<Category> findByCategoryName(String themeName){
        return categoryRepository.findByCategoryName(themeName);
    }

    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        return CategoryMapper.INSTANCE.toDTO(categoryRepository.save(
                Category.builder()
                        .categoryId(categoryDTO.getCategoryId())
                        .categoryName(categoryDTO.getCategoryName())
                        .build()
        ));
    }


    public String deleteCategory(int id){

        Optional<Category> category = categoryRepository.findById(id);
        if(category.isPresent()){
            categoryRepository.deleteById(id);
            return "Deleted " + category.get().getCategoryName() + " category successfully";
        }else{
            return "Not found category";
        }
    }

    public Page<CategoryDTO> getAllCategoryList(Pageable pageable) {
        Page<Category> categoryPage = categoryRepository.findAll(pageable);
        return categoryPage.map(CategoryMapper.INSTANCE::toDTO);
    }

    public List<CategoryDTO> searchCategory(String keyword) {
        List<Category> categoryList = categoryRepository.findByCategoryNameContainingIgnoreCase(keyword);
        return categoryList.stream().map(CategoryMapper.INSTANCE::toDTO).toList();
    }


    public Optional<CategoryDTO> getCategoryById(int id) {
        Optional<Category> category = categoryRepository.findById(id);
        if (category.isPresent()){
            CategoryDTO categoryDTO  = CategoryMapper.INSTANCE.toDTO(category.get());
            return Optional.ofNullable(categoryDTO);
        }
        return Optional.empty();
    }


    public CategoryDTO updateCategory(CategoryDTO categoryDTO){
        Optional<Category>  category =  categoryRepository.findById(categoryDTO.getCategoryId());
        if(category.isPresent()){
            Category data = CategoryMapper.INSTANCE.toEntity(categoryDTO);
            return CategoryMapper.INSTANCE.toDTO(categoryRepository.save(data));
        }
        return null;
    }



    public String saveCategoryFromCSV(@NotNull MultipartFile[] files) {
        if (files.length == 0) {
            return "Chưa chọn file!";
        }

        try {
            List<Category> categoryList = new ArrayList<>();
            Optional<Category> maxIdCategory = categoryRepository.findAll().stream()
                    .max(Comparator.comparingInt(Category::getCategoryId));
            int idCounter = maxIdCategory.map(category -> category.getCategoryId() + 1)
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
                    if (data.length < 1) {
                        continue;
                    }



                    String CategoryName = data[0].trim();
                    System.out.println("Đang xử lý tiện ích: " + CategoryName);

                    Category category = new Category();
                    category.setCategoryId(idCounter++);
                    category.setCategoryName(CategoryName);
                    categoryList.add(category);
                }
            }


            categoryRepository.saveAll(categoryList);
            return "" + categoryList.size();
        } catch (Exception e) {
            return "Lỗi xử lý file: " + e.getMessage();
        }
    }
}
