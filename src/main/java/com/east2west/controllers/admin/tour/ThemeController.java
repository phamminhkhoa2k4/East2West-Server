package com.east2west.controllers.admin.tour;


import com.east2west.models.DTO.ModelResponse;
import com.east2west.models.DTO.ThemeDTO;
import com.east2west.models.Entity.Theme;
import com.east2west.service.ThemeService;
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
@RequestMapping("/api/tours/themes")
public class ThemeController {

    private final ThemeService themeService;

    public ThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }



    // Endpoint: Create theme
    @PostMapping
    public ResponseEntity<ModelResponse<ThemeDTO>> createTheme(@RequestBody ThemeDTO theme) {
        try {
            Optional<Theme> themes = themeService.findByThemeName(theme.getThemeName());

            if(themes.isPresent()){
                return ResponseEntity.status(HttpStatus.OK).body(
                        ModelResponse.<ThemeDTO>builder()
                                .status(400)
                                .message("Theme name " + theme.getThemeName() + " already exists.")
                                .data(null)
                                .build()
                );
            }
            ThemeDTO data = themeService.createTheme(theme);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    ModelResponse.<ThemeDTO>builder()
                            .status(201)
                            .message(data.getThemeName() + " theme created successfully !!!")
                            .data(data)
                            .build()
            );
        }catch (Exception ex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    ModelResponse.<ThemeDTO>builder()
                            .status(400)
                            .message("Error Internal Server !!!")
                            .data(null)
                            .build()
            );
        }
    }


    // Endpoint: Delete theme by id
    @DeleteMapping("/{id}")
    public ResponseEntity<ModelResponse<Theme>> deleteTheme(@PathVariable int id) {
        try {
            String data = themeService.deleteTheme(id);
            if(data.startsWith("Deleted")){
                return ResponseEntity.status(HttpStatus.OK).body(
                        ModelResponse.<Theme>builder()
                                .status(200)
                                .message(data)
                                .data(null)
                                .build()
                );
            }else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        ModelResponse.<Theme>builder()
                                .status(404)
                                .message(data)
                                .data(null)
                                .build()
                );
            }

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ModelResponse.<Theme>builder()
                            .status(500)
                            .message("INTERNAL SERVER ERROR !!!")
                            .data(null)
                            .build()
            );

        }
    }

    // Endpoint: Pagination
    @GetMapping
    public Page<ThemeDTO> getThemes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return themeService.getAllThemes(PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "themeId")));
    }

    // Endpoint: Search theme
    @GetMapping("/search")
    public List<ThemeDTO> searchTheme(@RequestParam String keyword) {
        return themeService.searchTheme(keyword);
    }


    // Endpoint: Get a theme by id
    @GetMapping("/{id}")
    public ResponseEntity<ModelResponse<Optional<ThemeDTO>>> getThemeById(@PathVariable int id){
        try {
            Optional<ThemeDTO> data = themeService.getThemeById(id);
            if(data.isPresent()){
                return ResponseEntity.status(HttpStatus.OK).body(
                        ModelResponse.<Optional<ThemeDTO>>builder()
                                .status(200)
                                .message("OK")
                                .data(data)
                                .build()
                );
            }else{
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        ModelResponse.<Optional<ThemeDTO>>builder()
                                .status(404)
                                .message("Not found theme !!!")
                                .data(null)
                                .build()
                );
            }
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ModelResponse.<Optional<ThemeDTO>>builder()
                            .status(500)
                            .message("INTERNAL SERVER ERROR")
                            .data(null)
                            .build()
            );
        }

    }



    // Endpoint: Update theme
    @PutMapping
    public ResponseEntity<ModelResponse<ThemeDTO>> updateTheme(@RequestBody ThemeDTO theme) {
        try {
            ThemeDTO data = themeService.updateTheme(theme);
            if(data != null){
                return ResponseEntity.status(HttpStatus.OK).body(
                        ModelResponse.<ThemeDTO>builder()
                                .status(200)
                                .message(data.getThemeName() +" theme updated successfully !!!")
                                .data(data)
                                .build()
                );
            }else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        ModelResponse.<ThemeDTO>builder()
                                .status(404)
                                .message("Theme not found !!!")
                                .data(null)
                                .build()
                );
            }

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ModelResponse.<ThemeDTO>builder()
                            .status(500)
                            .message("INTERNAL SERVER ERROR")
                            .data(null)
                            .build()
            );

        }
    }


    // Endpoint: Create theme  by file csv
    @PostMapping("/uploadfromcsv")
    public ResponseEntity<ModelResponse<?>> uploadFile(@RequestParam("file") MultipartFile[] file) {
        try {

            String data = themeService.saveThemeFromCSV(file);

            return ResponseEntity.status(HttpStatus.CREATED).body(
                    ModelResponse.<ThemeDTO>builder()
                            .status(201)
                            .message("Create successfully " + data +" theme !!!").data(null).build());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ModelResponse.<ThemeDTO>builder()
                            .status(500)
                            .message(String.valueOf(e)).data(null).build());
        }
    }


    // Endpoint: List suitable
    @GetMapping("/list")
    public ResponseEntity<ModelResponse<List<ThemeDTO>>> listTheme(){
        try{
            List<ThemeDTO> data = themeService.getAllTheme();
            return ResponseEntity.status(HttpStatus.OK).body(
                    ModelResponse.<List<ThemeDTO>>builder().status(200).message("OK").data(data).build()
            );
        }catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ModelResponse.<List<ThemeDTO>>builder().status(200).message("INTERNAL SERVER ERROR").data(null).build()
            );
        }
    }
}
