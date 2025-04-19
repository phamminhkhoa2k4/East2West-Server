package com.east2west.controllers.admin.car;

import com.east2west.models.DTO.ModelDTO;
import com.east2west.models.DTO.ModelResponse;
import com.east2west.models.Entity.Model;
import com.east2west.service.ModelService;
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
@RequestMapping("/api/cars/model")
@PreAuthorize("hasAuthority('MODERATOR')")
public class ModelController {


    private final ModelService modelService;

    public ModelController(ModelService modelService) {
        this.modelService = modelService;
    }


    // Endpoint: Update model
    @PutMapping
    public ResponseEntity<ModelResponse<ModelDTO>> updateModel(@RequestBody Model model) {
        try {
            ModelDTO data = modelService.updateModel(model);
            if(data == null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        ModelResponse.<ModelDTO>builder()
                                .status(404)
                                .message("Model not found !!!")
                                .data(null)
                                .build()
                );

            }

            return ResponseEntity.status(HttpStatus.OK).body(
                    ModelResponse.<ModelDTO>builder()
                            .status(200)
                            .message(data.getModelname() +" model updated successfully !!!")
                            .data(data)
                            .build()
            );

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ModelResponse.<ModelDTO>builder()
                            .status(500)
                            .message("Failed to updated model !!!")
                            .data(null)
                            .build()
            );

        }
    }

    // Endpoint: Get a model by id
    @GetMapping("/{id}")
    public ResponseEntity<ModelResponse<Optional<ModelDTO>>> getModelById(@PathVariable int id){
        try {
            Optional<ModelDTO> data = modelService.getModelById(id);
            if(data.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        ModelResponse.<Optional<ModelDTO>>builder()
                                .status(404)
                                .message("Not found model !!!")
                                .data(null)
                                .build()
                );
            }
            return ResponseEntity.status(HttpStatus.OK).body(
                    ModelResponse.<Optional<ModelDTO>>builder()
                            .status(200)
                            .message("OK")
                            .data(data)
                            .build()
            );
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ModelResponse.<Optional<ModelDTO>>builder()
                            .status(500)
                            .message("INTERNAL SERVER ERROR")
                            .data(null)
                            .build()
            );
        }

    }

    // Endpoint: Create model by file csv
    @PostMapping("/uploadfromcsv")
    public ResponseEntity<ModelResponse<?>> uploadFile(@RequestParam("file") MultipartFile[] file) {
        try {

            String data = modelService.saveModelFromCSV(file);

            return ResponseEntity.status(HttpStatus.CREATED).body(
                    ModelResponse.<Model>builder()
                            .status(201)
                            .message("Create successfully " + data +" model !!!").data(null).build());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ModelResponse.<Model>builder()
                            .status(500)
                            .message(String.valueOf(e)).data(null).build());
        }
    }


    // Endpoint: Delete model by id
    @DeleteMapping("/{id}")
    public ResponseEntity<ModelResponse<Model>> deleteModel(@PathVariable int id) {
        try {
            String data = modelService.deleteModel(id);
            if(data.startsWith("Deleted")){
                return ResponseEntity.status(HttpStatus.OK).body(
                        ModelResponse.<Model>builder()
                                .status(200)
                                .message(data)
                                .data(null)
                                .build()
                );
            }
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        ModelResponse.<Model>builder()
                                .status(404)
                                .message(data)
                                .data(null)
                                .build()
                );


        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ModelResponse.<Model>builder()
                            .status(500)
                            .message("Failed to deleted model !!!")
                            .data(null)
                            .build()
            );

        }
    }

    // Endpoint: Pagination
    @GetMapping
    public Page<ModelDTO> getModels(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return modelService.getAllModels(PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "modelid")));
    }


    // Endpoint: Search model
    @GetMapping("/search")
    public List<ModelDTO> searchModel(@RequestParam String keyword) {
        return modelService.searchModel(keyword);
    }

    // Endpoint: Find all model by make id
    @GetMapping("make/{id}")
    public ResponseEntity<ModelResponse<List<ModelDTO>>> getAllModelByMakeId(@PathVariable int id){
        try {
            List<ModelDTO> data = modelService.findAllModelByMakeId(id);
            return ResponseEntity.status(HttpStatus.OK).body(
                    ModelResponse.<List<ModelDTO>>builder()
                            .status(200)
                            .message("OK")
                            .data(data)
                            .build()
            );
        }catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ModelResponse.<List<ModelDTO>>builder()
                            .status(500)
                            .message("INTERNAL SERVER ERROR !!!")
                            .data(null)
                            .build()
            );
        }
    }

    // Endpoint: Create model
    @PostMapping
    public ResponseEntity<ModelResponse<ModelDTO>> createModel(@RequestBody ModelDTO model) {
        try{

            Optional<Model> models = modelService.findByMakeIdAndModelName(model.getMake().getMakeid(),model.getModelname());
            if(models.isPresent()){
                return ResponseEntity.status(HttpStatus.OK).body(
                        ModelResponse.<ModelDTO>builder()
                                .status(400)
                                .message("Model name " + model.getModelname() + " already exists.")
                                .data(null)
                                .build()
                );
            }
            ModelDTO data = modelService.createModel(model);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    ModelResponse.<ModelDTO>builder()
                            .status(201)
                            .message(model.getModelname() + " model created successfully !!!")
                            .data(data)
                            .build()
            );
        }catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ModelResponse.<ModelDTO>builder()
                            .status(500)
                            .message("INTERNAL SERVER ERROR !!!")
                            .data(null)
                            .build()
            );
        }
    }
}
