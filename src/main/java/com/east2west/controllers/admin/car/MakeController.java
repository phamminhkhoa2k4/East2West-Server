package com.east2west.controllers.admin.car;

import com.east2west.models.DTO.MakeDTO;
import com.east2west.models.DTO.ModelResponse;
import com.east2west.models.Entity.Make;
import com.east2west.service.MakeService;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/api/cars/make")
@PreAuthorize("hasAuthority('MODERATOR')")
public class MakeController {

    private final MakeService makeService;

    @Autowired
    public MakeController(MakeService makeService) {
        this.makeService = makeService;
    }

    // Endpoint: Create make
    @PostMapping
    public ResponseEntity<ModelResponse<MakeDTO>> createMake(@RequestBody MakeDTO make) {
        try {
            Optional<Make> makes = makeService.findByMakeName(make.getMakename());

            if(makes.isPresent()){
                return ResponseEntity.status(HttpStatus.OK).body(
                        ModelResponse.<MakeDTO>builder()
                                .status(400)
                                .message("Make name " + make.getMakename() + " already exists.")
                                .data(null)
                                .build()
                );
            }
            MakeDTO data = makeService.createMake(make);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    ModelResponse.<MakeDTO>builder()
                            .status(201)
                            .message(data.getMakename() + " make created successfully !!!")
                            .data(data)
                            .build()
            );
        }catch (Exception ex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    ModelResponse.<MakeDTO>builder()
                            .status(400)
                            .message("Error Internal Server !!!")
                            .data(null)
                            .build()
            );
        }
    }


    // Endpoint: Update make
    @PutMapping
    public ResponseEntity<ModelResponse<MakeDTO>> updateMake(@RequestBody MakeDTO make) {
        try {
            MakeDTO data = makeService.updateMake(make);
            if(data == null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        ModelResponse.<MakeDTO>builder()
                                .status(404)
                                .message("Make not found !!!")
                                .data(null)
                                .build()
                );
            }
            return ResponseEntity.status(HttpStatus.OK).body(
                    ModelResponse.<MakeDTO>builder()
                            .status(200)
                            .message(data.getMakename() +" make updated successfully !!!")
                            .data(data)
                            .build()
            );

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ModelResponse.<MakeDTO>builder()
                            .status(500)
                            .message("INTERNAL SERVER ERROR")
                            .data(null)
                            .build()
            );

        }
    }


    // Endpoint: Get a make by id
    @GetMapping("/{id}")
    public ResponseEntity<ModelResponse<Optional<MakeDTO>>> getMakeById(@PathVariable int id){
        try {
            Optional<MakeDTO> data = makeService.getMakeById(id);
            if(data.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        ModelResponse.<Optional<MakeDTO>>builder()
                                .status(404)
                                .message("Not found make !!!")
                                .data(null)
                                .build()
                );
            }
            return ResponseEntity.status(HttpStatus.OK).body(
                    ModelResponse.<Optional<MakeDTO>>builder()
                            .status(200)
                            .message("OK")
                            .data(data)
                            .build()
            );
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ModelResponse.<Optional<MakeDTO>>builder()
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
            }

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    ModelResponse.<Make>builder()
                            .status(404)
                            .message(data)
                            .data(null)
                            .build()
            );

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ModelResponse.<Make>builder()
                            .status(500)
                            .message("INTERNAL SERVER ERROR !!!")
                            .data(null)
                            .build()
            );

        }
    }

    // Endpoint: Search make
    @GetMapping("/search")
    public List<MakeDTO> searchMake(@RequestParam String keyword) {
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
    public Page<MakeDTO> getMakes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return makeService.getAllMakes(PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "makeid")));
    }

    // Endpoint: List make
    @GetMapping("/list")
    public ResponseEntity<ModelResponse<List<MakeDTO>>> listMake(){
        try{
            List<MakeDTO> data = makeService.getAllMake();
            return ResponseEntity.status(HttpStatus.OK).body(
                    ModelResponse.<List<MakeDTO>>builder().status(200).message("OK").data(data).build()
            );
        }catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ModelResponse.<List<MakeDTO>>builder().status(200).message("INTERNAL SERVER ERROR").data(null).build()
            );
        }
    }


}
