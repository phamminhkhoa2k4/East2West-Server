package com.east2west.controllers.admin.tour;

import com.east2west.models.DTO.ModelResponse;
import com.east2west.models.DTO.SuitableDTO;
import com.east2west.models.Entity.Suitable;
import com.east2west.service.SuitableService;
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
@RequestMapping("/api/tours/suitable")
public class SuitableController {
    private final SuitableService suitableService;

    public SuitableController(SuitableService suitableService) {
        this.suitableService = suitableService;
    }



    // Endpoint: Create suitable
    @PostMapping
    public ResponseEntity<ModelResponse<SuitableDTO>> createSuitable(@RequestBody SuitableDTO suitableDto) {
        try {
            Optional<Suitable> suitable = suitableService.findBySuitableName(suitableDto.getSuitableName());

            if(suitable.isPresent()){
                return ResponseEntity.status(HttpStatus.OK).body(
                        ModelResponse.<SuitableDTO>builder()
                                .status(400)
                                .message("Suitable name " + suitableDto.getSuitableName() + " already exists.")
                                .data(null)
                                .build()
                );
            }
            SuitableDTO data = suitableService.createSuitable(suitableDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    ModelResponse.<SuitableDTO>builder()
                            .status(201)
                            .message(data.getSuitableName() + " suitable created successfully !!!")
                            .data(data)
                            .build()
            );
        }catch (Exception ex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    ModelResponse.<SuitableDTO>builder()
                            .status(400)
                            .message("Error Internal Server !!!")
                            .data(null)
                            .build()
            );
        }
    }


    // Endpoint: Delete suitable by id
    @DeleteMapping("/{id}")
    public ResponseEntity<ModelResponse<Suitable>> deleteSuitable(@PathVariable int id) {
        try {
            String data = suitableService.deleteSuitable(id);
            if(data.startsWith("Deleted")){
                return ResponseEntity.status(HttpStatus.OK).body(
                        ModelResponse.<Suitable>builder()
                                .status(200)
                                .message(data)
                                .data(null)
                                .build()
                );
            }else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        ModelResponse.<Suitable>builder()
                                .status(404)
                                .message(data)
                                .data(null)
                                .build()
                );
            }

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ModelResponse.<Suitable>builder()
                            .status(500)
                            .message("INTERNAL SERVER ERROR !!!")
                            .data(null)
                            .build()
            );

        }
    }

    // Endpoint: Pagination
    @GetMapping
    public Page<SuitableDTO> getSuitableList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return suitableService.getAllSuitableList(PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "suitableId")));
    }

    // Endpoint: Search suitable
    @GetMapping("/search")
    public List<SuitableDTO> searchSuitable(@RequestParam String keyword) {
        return suitableService.searchSuitable(keyword);
    }


    // Endpoint: Get a suitable by id
    @GetMapping("/{id}")
    public ResponseEntity<ModelResponse<Optional<SuitableDTO>>> getSuitableById(@PathVariable int id){
        try {
            Optional<SuitableDTO> data = suitableService.getSuitableById(id);
            if(data.isPresent()){
                return ResponseEntity.status(HttpStatus.OK).body(
                        ModelResponse.<Optional<SuitableDTO>>builder()
                                .status(200)
                                .message("OK")
                                .data(data)
                                .build()
                );
            }else{
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        ModelResponse.<Optional<SuitableDTO>>builder()
                                .status(404)
                                .message("Not found suitable !!!")
                                .data(null)
                                .build()
                );
            }
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ModelResponse.<Optional<SuitableDTO>>builder()
                            .status(500)
                            .message("INTERNAL SERVER ERROR")
                            .data(null)
                            .build()
            );
        }

    }



    // Endpoint: Update suitable
    @PutMapping
    public ResponseEntity<ModelResponse<SuitableDTO>> updateSuitable(@RequestBody SuitableDTO suitableDto) {
        try {
            SuitableDTO data = suitableService.updateSuitable(suitableDto);
            if(data != null){
                return ResponseEntity.status(HttpStatus.OK).body(
                        ModelResponse.<SuitableDTO>builder()
                                .status(200)
                                .message(data.getSuitableName() +" suitable updated successfully !!!")
                                .data(data)
                                .build()
                );
            }else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        ModelResponse.<SuitableDTO>builder()
                                .status(404)
                                .message("Suitable not found !!!")
                                .data(null)
                                .build()
                );
            }

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ModelResponse.<SuitableDTO>builder()
                            .status(500)
                            .message("INTERNAL SERVER ERROR")
                            .data(null)
                            .build()
            );

        }
    }


    // Endpoint: Create suitable  by file csv
    @PostMapping("/uploadfromcsv")
    public ResponseEntity<ModelResponse<?>> uploadFile(@RequestParam("file") MultipartFile[] file) {
        try {

            String data = suitableService.saveSuitableFromCSV(file);

            return ResponseEntity.status(HttpStatus.CREATED).body(
                    ModelResponse.<SuitableDTO>builder()
                            .status(201)
                            .message("Create successfully " + data +" suitable !!!").data(null).build());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ModelResponse.<SuitableDTO>builder()
                            .status(500)
                            .message(String.valueOf(e)).data(null).build());
        }
    }
}
