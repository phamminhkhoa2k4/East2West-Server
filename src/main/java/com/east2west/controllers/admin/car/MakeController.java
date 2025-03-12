package com.east2west.controllers.admin.car;

import com.east2west.models.DTO.ModelResponse;
import com.east2west.models.Entity.Make;
import com.east2west.service.MakeService;
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
@RequestMapping("/api/cars/make")
public class MakeController {

    private final MakeService makeService;

    @Autowired
    public MakeController(MakeService makeService) {
        this.makeService = makeService;
    }

    // Endpoint: Update make
    @PostMapping
    public ResponseEntity<ModelResponse<Make>> createMake(@RequestBody Make make) {
        try {
            Optional<Make> makes = makeService.findByMakeName(make.getMakename());

            if(makes.isPresent()){
                return ResponseEntity.status(HttpStatus.CREATED).body(
                        ModelResponse.<Make>builder()
                                .status(400)
                                .message("Make name " + make.getMakename() + " already exists.")
                                .data(null)
                                .build()
                );
            }
            Make data = makeService.saveMake(make);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    ModelResponse.<Make>builder()
                            .status(201)
                            .message(data.getMakename() + " make created successfully !!!")
                            .data(data)
                            .build()
            );
        }catch (Exception ex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    ModelResponse.<Make>builder()
                            .status(400)
                            .message("Error Internal Server !!!")
                            .data(null)
                            .build()
            );
        }
    }


    // Endpoint: Update amenities
    @PutMapping
    public ResponseEntity<ModelResponse<Make>> updateAmenities(@RequestBody Make make) {
        try {
            Make data = makeService.updateMake(make);
            if(data != null){
                return ResponseEntity.status(HttpStatus.OK).body(
                        ModelResponse.<Make>builder()
                                .status(200)
                                .message(data.getMakename() +" make updated successfully !!!")
                                .data(data)
                                .build()
                );
            }else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        ModelResponse.<Make>builder()
                                .status(404)
                                .message("Make not found !!!")
                                .data(null)
                                .build()
                );
            }

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ModelResponse.<Make>builder()
                            .status(500)
                            .message("Failed to updated make !!!")
                            .data(null)
                            .build()
            );

        }
    }


    // Endpoint: Get an amenities by id
    @GetMapping("/{id}")
    public ResponseEntity<ModelResponse<Optional<Make>>> getMakeById(@PathVariable int id){
        try {
            Optional<Make> data = makeService.getByIdMake(id);
            if(data.isPresent()){
                return ResponseEntity.status(HttpStatus.OK).body(
                        ModelResponse.<Optional<Make>>builder()
                                .status(200)
                                .message("OK")
                                .data(data)
                                .build()
                );
            }else{
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        ModelResponse.<Optional<Make>>builder()
                                .status(404)
                                .message("Not found make !!!")
                                .data(null)
                                .build()
                );
            }
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ModelResponse.<Optional<Make>>builder()
                            .status(500)
                            .message("INTERNAL SERVER ERROR")
                            .data(null)
                            .build()
            );
        }

    }

    // Endpoint: Delete make by id
    @DeleteMapping("/{id}")
    public ResponseEntity<ModelResponse<Make>> deleteMake(@PathVariable int id) {
        try {
            String data = makeService.deleteMake(id);
            if(data.startsWith("Deleted")){
                return ResponseEntity.status(HttpStatus.OK).body(
                        ModelResponse.<Make>builder()
                                .status(200)
                                .message(data)
                                .data(null)
                                .build()
                );
            }else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        ModelResponse.<Make>builder()
                                .status(404)
                                .message(data)
                                .data(null)
                                .build()
                );
            }

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ModelResponse.<Make>builder()
                            .status(500)
                            .message("Failed to deleted amenities !!!")
                            .data(null)
                            .build()
            );

        }
    }

    // Endpoint: Search make
    @GetMapping("/search")
    public List<Make> searchAmenities(@RequestParam String keyword) {
        return makeService.searchMake(keyword);
    }

    // Endpoint: Create make by file csv
    @PostMapping("/uploadfromcsv")
    public ResponseEntity<ModelResponse<?>> uploadFile(@RequestParam("file") MultipartFile[] file) {
        try {

            String data = makeService.saveMakeFromCSV(file);

            return ResponseEntity.status(HttpStatus.CREATED).body(
                    ModelResponse.<Make>builder()
                            .status(201)
                            .message("Create successfully " + data +" make !!!").data(null).build());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ModelResponse.<Make>builder()
                            .status(500)
                            .message(String.valueOf(e)).data(null).build());
        }
    }

    // Endpoint: Pagination
    @GetMapping
    public Page<Make> getMakes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<Make> result = makeService.getAllMakes(PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "makeid")));
        System.out.println(result);
        return result;
    }


}
