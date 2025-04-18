package com.east2west.controllers.admin.car;

import com.east2west.models.DTO.ModelResponse;
import com.east2west.models.Entity.Type;
import com.east2west.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/api/cars/type")
@PreAuthorize("hasAuthority('MODERATOR')")
public class TypeController {

    private final TypeService typeService;


    @Autowired
    public TypeController(TypeService typeService) {
        this.typeService = typeService;
    }


    // Endpoint: Create type
    @PostMapping
    public ResponseEntity<ModelResponse<Type>> createType(@RequestBody Type type) {
        try {
            Optional<Type> types = typeService.findByTypeName(type.getTypename());

            if(types.isPresent()){
                return ResponseEntity.status(HttpStatus.OK).body(
                        ModelResponse.<Type>builder()
                                .status(400)
                                .message("Make name " + type.getTypename() + " already exists.")
                                .data(null)
                                .build()
                );
            }
            Type data = typeService.saveType(type);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    ModelResponse.<Type>builder()
                            .status(201)
                            .message(data.getTypename() + " type created successfully !!!")
                            .data(data)
                            .build()
            );
        }catch (Exception ex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    ModelResponse.<Type>builder()
                            .status(400)
                            .message("Error Internal Server !!!")
                            .data(null)
                            .build()
            );
        }
    }

    // Endpoint: Update type
    @PutMapping
    public ResponseEntity<ModelResponse<Type>> updateType(@RequestBody Type type) {
        try {
            Type data = typeService.updateType(type);
            if(data != null){
                return ResponseEntity.status(HttpStatus.OK).body(
                        ModelResponse.<Type>builder()
                                .status(200)
                                .message(data.getTypename() +" type updated successfully !!!")
                                .data(data)
                                .build()
                );
            }else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        ModelResponse.<Type>builder()
                                .status(404)
                                .message("Type not found !!!")
                                .data(null)
                                .build()
                );
            }

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ModelResponse.<Type>builder()
                            .status(500)
                            .message("INTERNAL SERVER ERROR !!!")
                            .data(null)
                            .build()
            );

        }
    }


    // Endpoint: Get a type by id
    @GetMapping("/{id}")
    public ResponseEntity<ModelResponse<Optional<Type>>> getTypeById(@PathVariable int id){
        try {
            Optional<Type> data = typeService.getByIdType(id);
            if(data.isPresent()){
                return ResponseEntity.status(HttpStatus.OK).body(
                        ModelResponse.<Optional<Type>>builder()
                                .status(200)
                                .message("OK")
                                .data(data)
                                .build()
                );
            }else{
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        ModelResponse.<Optional<Type>>builder()
                                .status(404)
                                .message("Not found type !!!")
                                .data(null)
                                .build()
                );
            }
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ModelResponse.<Optional<Type>>builder()
                            .status(500)
                            .message("INTERNAL SERVER ERROR !!!")
                            .data(null)
                            .build()
            );
        }

    }

    // Endpoint: Delete type by id
    @DeleteMapping("/{id}")
    public ResponseEntity<ModelResponse<Type>> deleteType(@PathVariable int id) {
        try {
            String data = typeService.deleteType(id);
            if(data.startsWith("Deleted")){
                return ResponseEntity.status(HttpStatus.OK).body(
                        ModelResponse.<Type>builder()
                                .status(200)
                                .message(data)
                                .data(null)
                                .build()
                );
            }else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        ModelResponse.<Type>builder()
                                .status(404)
                                .message(data)
                                .data(null)
                                .build()
                );
            }

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ModelResponse.<Type>builder()
                            .status(500)
                            .message("INTERNAL SERVER ERROR !!!")
                            .data(null)
                            .build()
            );

        }
    }

    // Endpoint: Search type
    @GetMapping("/search")
    public ResponseEntity<List<Type>> searchType(@RequestParam String keyword) {
        return ResponseEntity.status(HttpStatus.OK).body(typeService.searchType(keyword));
    }

    // Endpoint: Create make by file csv
    @PostMapping("/uploadfromcsv")
    public ResponseEntity<ModelResponse<?>> uploadFile(@RequestParam("file") MultipartFile[] file) {
        try {

            String data = typeService.saveTypeFromCSV(file);

            return ResponseEntity.status(HttpStatus.CREATED).body(
                    ModelResponse.<Type>builder()
                            .status(201)
                            .message("Create successfully " + data +" type !!!").data(null).build());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ModelResponse.<Type>builder()
                            .status(500)
                            .message(String.valueOf(e)).data(null).build());
        }
    }

    // Endpoint: Pagination
    @GetMapping
    public ResponseEntity<Page<Type>> getTypes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<Type> types = typeService.getAllTypes(PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "typeid")));
        return ResponseEntity.ok(types);
    }

    // Endpoint: List type
    @GetMapping("/list")
    public ResponseEntity<ModelResponse<List<Type>>> listType(){
        try{
            List<Type> data = typeService.getAllType();
            return ResponseEntity.status(HttpStatus.OK).body(
                    ModelResponse.<List<Type>>builder().status(200).message("OK").data(data).build()
            );
        }catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ModelResponse.<List<Type>>builder().status(200).message("INTERNAL SERVER ERROR").data(null).build()
            );
        }
    }


}