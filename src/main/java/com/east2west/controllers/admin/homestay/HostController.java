package com.east2west.controllers.admin.homestay;


import com.east2west.models.DTO.AmenitiesDTO;
import com.east2west.models.DTO.HomestayDTO;
import com.east2west.models.DTO.ModelResponse;
import com.east2west.models.DTO.PhotoDeleteDTO;
import com.east2west.models.Entity.Homestay;
import com.east2west.models.Entity.Structure;
import com.east2west.models.Entity.User;
import com.east2west.models.enums.EHomestayStatus;
import com.east2west.models.payload.request.IdentityUploadRequest;
import com.east2west.models.payload.request.IdentityVerifyAutoRequest;
import com.east2west.models.payload.request.IdentityVerifyManualRequest;
import com.east2west.security.jwt.JwtUtils;
import com.east2west.service.AmenitiesService;
import com.east2west.service.HomestayService;
import com.east2west.service.StructureService;
import com.east2west.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/homestays/host")
public class HostController {

    private final HomestayService homestayService;

    private final AmenitiesService amenitiesService;

    private final StructureService structureService;

    private final JwtUtils jwtUtils;

    private final UserService userService;
    @Autowired
    public HostController(HomestayService homestayService, AmenitiesService amenitiesService , StructureService structureService, JwtUtils jwtUtils, UserService userService) {
        this.homestayService = homestayService;
        this.amenitiesService = amenitiesService;
        this.structureService = structureService;
        this.jwtUtils = jwtUtils;
        this.userService = userService;
    }

    @PutMapping("/{id}")
    public ResponseEntity<ModelResponse<Homestay>> updateHomestay(@PathVariable int id , @RequestBody HomestayDTO homestayDTO) {
       try{
           Homestay homestay = homestayService.updateHomestay(id,homestayDTO);
           return ResponseEntity.status(HttpStatus.OK).body(
                   ModelResponse.<Homestay>builder()
                           .status(201)
                           .message("OK")
                           .data(homestay)
                           .build());
       }catch (Exception e){
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                   ModelResponse.<Homestay>builder()
                           .status(500)
                           .message("INTERNAL SERVER ERROR")
                           .data(null)
                           .build());
       }
    }
    @PostMapping
    public ResponseEntity<ModelResponse<HomestayDTO>> createHomestay(@Valid @RequestBody HomestayDTO homestayDTO) {
        try{
            HomestayDTO homestay = homestayService.createHomestay(homestayDTO);
            return ResponseEntity.status(HttpStatus.OK).body(
                    ModelResponse.<HomestayDTO>builder()
                            .status(201)
                            .message("OK")
                            .data(homestay)
                            .build());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ModelResponse.<HomestayDTO>builder()
                            .status(500)
                            .message("INTERNAL SERVER ERROR")
                            .data(null)
                            .build());
        }

    }

    @PutMapping("/user/upload-identity")
    public  ResponseEntity<ModelResponse<?>> uploadIdentity(@RequestBody IdentityUploadRequest identity) {
        try {
                Optional<User> user = userService.getUserById(Integer.parseInt(identity.getUserId()));

                if(user.isEmpty()) {
                    return ResponseEntity.status(HttpStatus.OK).body(
                            ModelResponse.builder()
                                    .status(404)
                                    .data(null)
                                    .message("User Not Found !!!")
                                    .build()
                    );
                }

                User data = userService.saveIdentity(identity,user.get());
                return ResponseEntity.status(HttpStatus.OK).body(
                    ModelResponse.builder()
                            .status(200)
                            .data(data.getUsername())
                            .message("OK")
                            .build()
                );


        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ModelResponse.builder()
                            .status(500)
                            .data(null)
                            .message("INTERNAL SERVER ERROR")
                            .build()

            );
        }
    }


    @PutMapping("/user/manual-verify-identity")
    public  ResponseEntity<ModelResponse<?>> verifyIdentityManual(@RequestBody IdentityVerifyManualRequest identity) {
        try {
            Optional<User> user = userService.getUserById(Integer.parseInt(identity.getUserId()));

            if(user.isEmpty()) {
                return ResponseEntity.status(HttpStatus.OK).body(
                        ModelResponse.builder()
                                .status(404)
                                .data(null)
                                .message("User Not Found !!!")
                                .build()
                );
            }

            User data = userService.manualIdentificationVerification(identity,user.get());
            return ResponseEntity.status(HttpStatus.OK).body(
                    ModelResponse.builder()
                            .status(200)
                            .data(data.getUsername())
                            .message("OK")
                            .build()
            );


        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ModelResponse.builder()
                            .status(500)
                            .data(null)
                            .message("INTERNAL SERVER ERROR")
                            .build()

            );
        }
    }


    @PutMapping("/user/automation-verify-identity")
    public  ResponseEntity<ModelResponse<?>> verifyIdentityAuto(@RequestBody IdentityVerifyAutoRequest identity) {
        try {
            Optional<User> user = userService.getUserById(Integer.parseInt(identity.getUserId()));

            if(user.isEmpty()) {
                return ResponseEntity.status(HttpStatus.OK).body(
                        ModelResponse.builder()
                                .status(404)
                                .data(null)
                                .message("User Not Found !!!")
                                .build()
                );
            }

            User data = userService.automaticIdentificationVerification(identity,user.get());
            return ResponseEntity.status(HttpStatus.OK).body(
                    ModelResponse.builder()
                            .status(200)
                            .data(data.getUsername())
                            .message("OK")
                            .build()
            );


        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ModelResponse.builder()
                            .status(500)
                            .data(null)
                            .message("INTERNAL SERVER ERROR")
                            .build()

            );
        }
    }

    @GetMapping("/user/{id}/{status}")
    public ResponseEntity<ModelResponse<List<HomestayDTO>>> inProgress(@PathVariable("id") int id, @PathVariable("status") EHomestayStatus status){
         try{
             Optional<User> user = userService.getUserById(id);
             if(user.isEmpty()) {
                 return ResponseEntity.status(HttpStatus.OK).body(
                         ModelResponse.<List<HomestayDTO>>builder()
                                 .status(404)
                                 .message("User Not Found !!!")
                                 .data(null)
                                 .build()
                 );
             }
             List<HomestayDTO> homestays = homestayService.getAllHomestayStatus(id,status);

             return ResponseEntity.status(HttpStatus.OK).body(
                     ModelResponse.<List<HomestayDTO>>builder()
                             .status(200)
                             .message("OK")
                             .data(homestays)
                             .build()
             );

         }catch (Exception e){
             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                     ModelResponse.<List<HomestayDTO>>builder()
                             .status(500)
                             .data(null)
                             .message("INTERNAL SERVER ERROR")
                             .build()
             );
         }
    }


    @GetMapping("/user/identity-forward/{id}")
    public ResponseEntity<ModelResponse<?>> getIdentityForward(@PathVariable int id){
        try{
            Optional<User> user = userService.getUserById(id);

            if(user.isEmpty()) {
                return ResponseEntity.status(HttpStatus.OK).body(
                        ModelResponse.builder().data(null).status(404).message("User not found !!!").build()
                );
            }

            String data = userService.getIdentityForward(id);
            return ResponseEntity.status(HttpStatus.OK).body(
                    ModelResponse.builder().status(200).data(data).message("OK").build()
            );
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ModelResponse.builder().status(500).data(null).message("INTERNAL SERVER ERROR").build()
            );
        }
    }


    
    @DeleteMapping("/{id}")
    public ResponseEntity<ModelResponse<Homestay>> deleteHomestay(@PathVariable int id) {
        try {
            String data =  homestayService.deleteHomestay(id);
            if(data.startsWith("Deleted")){
                return ResponseEntity.status(HttpStatus.OK).body(
                        ModelResponse.<Homestay>builder()
                                .status(200)
                                .message(data)
                                .data(null)
                                .build()
                );
            }else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        ModelResponse.<Homestay>builder()
                                .status(404)
                                .message(data)
                                .data(null)
                                .build()
                );
            }

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ModelResponse.<Homestay>builder()
                            .status(500)
                            .message("Failed to deleted homestay !!!")
                            .data(null)
                            .build()
            );

        }
    }

    @DeleteMapping("/deletePhotos")
    public ResponseEntity<?> deletePhotosHomestay(@RequestBody PhotoDeleteDTO photo) {
        try {
            homestayService.deletePhotos(photo.getUrl(), photo.getId());
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<ModelResponse<List<HomestayDTO>>> getAllHomestaysByIdUser(@PathVariable int id){
        try {
            Optional<User> user = userService.getUserById(id);
            if(user.isEmpty()) {
                return ResponseEntity.status(HttpStatus.OK).body(
                        ModelResponse.<List<HomestayDTO>>builder()
                                .status(404)
                                .message("User Not Found !!!")
                                .data(null)
                                .build()
                );
            }

            List<HomestayDTO> homestay = homestayService.getAllByIdUser(id);

            return ResponseEntity.status(HttpStatus.OK).body(
                    ModelResponse.<List<HomestayDTO>>builder()
                            .status(200)
                            .message("OK")
                            .data(homestay)
                            .build()
            );
    

        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ModelResponse.<List<HomestayDTO>>builder()
                            .status(500)
                            .message("INTERNAL SERVER ERROR")
                            .data(null)
                            .build()
            );
        }


    }


    @PostMapping("/baseprice")
    public ResponseEntity<?> updateBasePrice(@RequestBody HomestayDTO homestayDTO){
        try {
            homestayService.updateBasePrice(homestayDTO.getHomestayid(), homestayDTO.getPricePerNight());
            return ResponseEntity.ok("Price updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while updating the price");
        }
    }

    @PostMapping("/weekendprice")
    public ResponseEntity<?> updateWeekendPrice(@RequestBody HomestayDTO homestayDTO){
        try {
            homestayService.updateWeekendPrice(homestayDTO.getHomestayid(), homestayDTO.getPricePerNight());
            return ResponseEntity.ok("Price updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while updating the price");
        }
    }

    // Endpoint: Get all amenities
    @GetMapping("/amenity")
    public ResponseEntity<ModelResponse<List<AmenitiesDTO>>> getAllAmenities(){
        try {
            List<AmenitiesDTO> data = amenitiesService.getAmenitiesAll();
            return ResponseEntity.status(HttpStatus.OK).body(
                    ModelResponse.<List<AmenitiesDTO>>builder()
                            .status(200)
                            .message("OK")
                            .data(data)
                            .build()
            );
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ModelResponse.<List<AmenitiesDTO>>builder()
                            .status(500)
                            .message("INTERNAL SERVER ERROR")
                            .data(null)
                            .build()
            );
        }
    }

    @GetMapping("/structures")
    public ResponseEntity<ModelResponse<List<Structure>>> getAllStructure(){
        try {
            List<Structure> data = structureService.getAllStructure();
            return ResponseEntity.status(HttpStatus.OK).body(
                    ModelResponse.<List<Structure>>builder()
                            .status(200)
                            .message("OK")
                            .data(data)
                            .build()
            );
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ModelResponse.<List<Structure>>builder()
                            .status(500)
                            .message("INTERNAL SERVER ERROR")
                            .data(null)
                            .build()
            );
        }
    }

    @GetMapping("/search")
    public List<HomestayDTO> search(@RequestHeader("Authorization") String authorizationHeader,@RequestParam(name = "query", required = false) String keyword){
        String token = authorizationHeader != null && authorizationHeader.startsWith("Bearer ")
                ? authorizationHeader.substring(7)
                : null;
        String usernameFromToken = jwtUtils.getUserNameFromJwtToken(token);
        Optional<User> user =  userService.findByUsername(usernameFromToken);

        return user.map(value -> homestayService.search(keyword, value.getUserId())).orElse(null);
    }
}
