package com.east2west.controllers.admin.car;

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
    public ResponseEntity<ModelResponse<Model>> updateModel(@RequestBody Model model) {
        try {
            Model data = modelService.updateModel(model);
            if(data != null){
                return ResponseEntity.status(HttpStatus.OK).body(
                        ModelResponse.<Model>builder()
                                .status(200)
                                .message(data.getModelname() +" model updated successfully !!!")
                                .data(data)
                                .build()
                );
            }else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        ModelResponse.<Model>builder()
                                .status(404)
                                .message("Model not found !!!")
                                .data(null)
                                .build()
                );
            }

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ModelResponse.<Model>builder()
                            .status(500)
                            .message("Failed to updated model !!!")
                            .data(null)
                            .build()
            );

        }
    }

    // Endpoint: Get a model by id
    @GetMapping("/{id}")
    public ResponseEntity<ModelResponse<Optional<Model>>> getModelById(@PathVariable int id){
        try {
            Optional<Model> data = modelService.getByIdModel(id);
            if(data.isPresent()){
                return ResponseEntity.status(HttpStatus.OK).body(
                        ModelResponse.<Optional<Model>>builder()
                                .status(200)
                                .message("OK")
                                .data(data)
                                .build()
                );
            }else{
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        ModelResponse.<Optional<Model>>builder()
                                .status(404)
                                .message("Not found model !!!")
                                .data(null)
                                .build()
                );
            }
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ModelResponse.<Optional<Model>>builder()
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
            }else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        ModelResponse.<Model>builder()
                                .status(404)
                                .message(data)
                                .data(null)
                                .build()
                );
            }

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
    public Page<Model> getModels(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return modelService.getAllModels(PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "modelid")));
    }


    // Endpoint: Search model
    @GetMapping("/search")
    public List<Model> searchModel(@RequestParam String keyword) {
        return modelService.searchModel(keyword);
    }


    // Endpoint: Create model
    @PostMapping
    public ResponseEntity<ModelResponse<Model>> createModel(@RequestBody Model model) {
        try{

            Optional<Model> models = modelService.findByMakeIdAndModelName(model.getMake().getMakeid(),model.getModelname());
            if(models.isPresent()){
                return ResponseEntity.status(HttpStatus.OK).body(
                        ModelResponse.<Model>builder()
                                .status(400)
                                .message("Model name " + model.getModelname() + " already exists.")
                                .data(null)
                                .build()
                );
            }
            Model data = modelService.createModel(model);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    ModelResponse.<Model>builder()
                            .status(201)
                            .message(model.getModelname() + " make created successfully !!!")
                            .data(data)
                            .build()
            );
        }catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ModelResponse.<Model>builder()
                            .status(500)
                            .message("INTERNAL SERVER ERROR !!!")
                            .data(null)
                            .build()
            );
        }
    }
}
