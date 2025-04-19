package com.east2west.controllers.admin.homestay;


import com.east2west.models.DTO.ModelResponse;
import com.east2west.models.DTO.StructureDTO;
import com.east2west.models.Entity.Structure;
import com.east2west.service.StructureService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/homestays/host/structure")
@PreAuthorize("hasAuthority('MODERATOR')")
public class StructureController {

    private final StructureService structureService;

    public StructureController(StructureService structureService){
        this.structureService = structureService;
    }

    @PutMapping
    public ResponseEntity<ModelResponse<StructureDTO>> updateStructure(@RequestBody StructureDTO structure) {
        try {
            StructureDTO data = structureService.updateStructure(structure);
            if(data == null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        ModelResponse.<StructureDTO>builder()
                                .status(404)
                                .message("Structure not found !!!")
                                .data(null)
                                .build()
                );

            }

            return ResponseEntity.status(HttpStatus.OK).body(
                    ModelResponse.<StructureDTO>builder()
                            .status(200)
                            .message(data.getStructurename() +" structure updated successfully !!!")
                            .data(data)
                            .build()
            );

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ModelResponse.<StructureDTO>builder()
                            .status(500)
                            .message("Failed to updated structure !!!")
                            .data(null)
                            .build()
            );

        }


    }

    @PostMapping
    public ResponseEntity<ModelResponse<StructureDTO>> createStructure(@RequestBody StructureDTO structure) {
        try {
            StructureDTO data = structureService.createStructure(structure);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    ModelResponse.<StructureDTO>builder()
                            .status(201)
                            .message(data.getStructurename() + " structure created successfully !!!")
                            .data(data)
                            .build()
            );
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ModelResponse.<StructureDTO>builder()
                            .status(500)
                            .message("Failed to create structure !!!")
                            .data(null)
                            .build()
            );

        }

    }

    @GetMapping("/{id}")
    public ResponseEntity<ModelResponse<Optional<StructureDTO>>> getStructureById(@PathVariable int id){
        try {
            Optional<StructureDTO> data = structureService.getStructureById(id);
            if(data.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        ModelResponse.<Optional<StructureDTO>>builder()
                                .status(404)
                                .message("Not found structure !!!")
                                .data(null)
                                .build()
                );

            }
            return ResponseEntity.status(HttpStatus.OK).body(
                    ModelResponse.<Optional<StructureDTO>>builder()
                            .status(200)
                            .message("OK")
                            .data(data)
                            .build()
            );
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ModelResponse.<Optional<StructureDTO>>builder()
                            .status(500)
                            .message("INTERNAL SERVER ERROR")
                            .data(null)
                            .build()
            );
        }

    }


    @GetMapping("/search")
    public List<StructureDTO> searchStructure(@RequestParam String keyword) {
        return structureService.searchStructure(keyword);
    }

    // Endpoint: Pagination
    @GetMapping
    public Page<StructureDTO> getStructure(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return structureService.getAllStructure(PageRequest.of(page, size, Sort.by(Sort.Direction.DESC,"structureid")));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ModelResponse<StructureDTO>> deleteStructure(@PathVariable int id) {
        try {
            String data = structureService.deleteStructure(id);
            if(data.startsWith("Deleted")){
                return ResponseEntity.status(HttpStatus.OK).body(
                        ModelResponse.<StructureDTO>builder()
                                .status(200)
                                .message(data)
                                .data(null)
                                .build()
                );
            }
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        ModelResponse.<StructureDTO>builder()
                                .status(404)
                                .message(data)
                                .data(null)
                                .build()
                );


        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ModelResponse.<StructureDTO>builder()
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
