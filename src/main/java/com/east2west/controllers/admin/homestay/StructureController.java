package com.east2west.controllers.admin.homestay;


import com.east2west.models.DTO.ModelResponse;
import com.east2west.models.Entity.Structure;
import com.east2west.service.StructureService;
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
@RequestMapping("/api/homestays/host/structure")
public class StructureController {

    private final StructureService structureService;

    public StructureController(StructureService structureService){
        this.structureService = structureService;
    }
    //
    @PutMapping
    public ResponseEntity<ModelResponse<Structure>> updateStructure(@RequestBody Structure structure) {
        try {
            Structure data = structureService.updateStructure(structure);
            if(data != null){
                return ResponseEntity.status(HttpStatus.OK).body(
                        ModelResponse.<Structure>builder()
                                .status(200)
                                .message(data.getStructurename() +" structure updated successfully !!!")
                                .data(data)
                                .build()
                );
            }else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        ModelResponse.<Structure>builder()
                                .status(404)
                                .message("Structure not found !!!")
                                .data(null)
                                .build()
                );
            }

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ModelResponse.<Structure>builder()
                            .status(500)
                            .message("Failed to updated structure !!!")
                            .data(null)
                            .build()
            );

        }


    }

    @PostMapping
    public ResponseEntity<ModelResponse<Structure>> createStructure(@RequestBody Structure structure) {
        try {
            Structure data = structureService.createStructure(structure);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    ModelResponse.<Structure>builder()
                            .status(201)
                            .message(data.getStructurename() + " structure created successfully !!!")
                            .data(data)
                            .build()
            );
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ModelResponse.<Structure>builder()
                            .status(500)
                            .message("Failed to create structure !!!")
                            .data(null)
                            .build()
            );

        }

    }

    @GetMapping("/{id}")
    public ResponseEntity<ModelResponse<Optional<Structure>>> getStructureById(@PathVariable int id){
        try {
            Optional<Structure> data = structureService.getByIdStructure(id);
            if(data.isPresent()){
                return ResponseEntity.status(HttpStatus.OK).body(
                        ModelResponse.<Optional<Structure>>builder()
                                .status(200)
                                .message("OK")
                                .data(data)
                                .build()
                );
            }else{
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        ModelResponse.<Optional<Structure>>builder()
                                .status(404)
                                .message("Not found structure !!!")
                                .data(null)
                                .build()
                );
            }
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ModelResponse.<Optional<Structure>>builder()
                            .status(500)
                            .message("INTERNAL SERVER ERROR")
                            .data(null)
                            .build()
            );
        }

    }

//    @GetMapping
//    public ResponseEntity<List<Structure>> getAllStructure(){
//        List<Structure> structures= structureService.getStructureAll();
//        return ResponseEntity.ok(structures);
//    }

    @GetMapping("/search")
    public List<Structure> searchStructure(@RequestParam String keyword) {
        return structureService.searchStructure(keyword);
    }

    // Endpoint: Pagination
    @GetMapping
    public Page<Structure> getStructure(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return structureService.getAllStructure(PageRequest.of(page, size, Sort.by(Sort.Direction.DESC,"structureid")));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ModelResponse<Structure>> deleteStructure(@PathVariable int id) {
        try {
            String data = structureService.deleteStructure(id);
            if(data.startsWith("Deleted")){
                return ResponseEntity.status(HttpStatus.OK).body(
                        ModelResponse.<Structure>builder()
                                .status(200)
                                .message(data)
                                .data(null)
                                .build()
                );
            }else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        ModelResponse.<Structure>builder()
                                .status(404)
                                .message(data)
                                .data(null)
                                .build()
                );
            }

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ModelResponse.<Structure>builder()
                            .status(500)
                            .message("Failed to deleted structure !!!")
                            .data(null)
                            .build()
            );

        }
    }

    // Endpoint: Create structure by file csv
    @PostMapping("/uploadfromcsv")
    public ResponseEntity<ModelResponse<?>> uploadFile(@RequestParam("file") MultipartFile[] file) {
        try {

            String data = structureService.saveStructureFromCSV(file);

            return ResponseEntity.status(HttpStatus.CREATED).body(
                    ModelResponse.<Structure>builder()
                            .status(201)
                            .message("Create successfully " + data +" structure !!!").data(null).build());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ModelResponse.<Structure>builder()
                            .status(500)
                            .message(String.valueOf(e)).data(null).build());
        }
    }
}
