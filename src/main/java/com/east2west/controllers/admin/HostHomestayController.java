
package com.east2west.controllers.admin;

import com.east2west.models.DTO.PhotoDeleteDTO;
import com.east2west.models.Entity.Amenities;
import com.east2west.models.Entity.Structure;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.east2west.models.DTO.HomestayDTO;
import com.east2west.models.Entity.Homestay;
import com.east2west.service.HomestayService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/homestays/host")
public class HostHomestayController {
    private final HomestayService homestayService;



    @Autowired
    public HostHomestayController(HomestayService homestayService) {
        this.homestayService = homestayService;
    }
    @PutMapping("/{id}")
    public ResponseEntity<Homestay> updateHomestay(@PathVariable int id , @RequestBody HomestayDTO homestayDTO) {
        Homestay homestay = homestayService.updateHomestay(id,homestayDTO);
        return ResponseEntity.ok(homestay);
    }
    @PostMapping
    public ResponseEntity<Homestay> createHomestay(@RequestBody HomestayDTO homestayDTO) {
        homestayDTO.setGeom(null);
        homestayDTO.setHomestayid(null);
        Homestay homestay = homestayService.createHomestay(homestayDTO);
        return ResponseEntity.ok(homestay);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHomestay(@PathVariable int id) {
        homestayService.deleteHomestay(id);
        return ResponseEntity.noContent().build();
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



    @PutMapping("/amenities")
    public ResponseEntity<Amenities> updateAmenities(@RequestBody Amenities amenities) {
        Amenities homestay = homestayService.updateAmenities(amenities);
        return ResponseEntity.ok(homestay);
    }

    @PutMapping("/structure")
    public ResponseEntity<Structure> updateStructure(@RequestBody Structure structure) {
        Structure homestay = homestayService.updateStructure(structure);
        return ResponseEntity.ok(homestay);
    }

    @PostMapping("/structure")
    public ResponseEntity<Structure> createStructure(@RequestBody Structure structure) {
        Structure homestay = homestayService.createStructure(structure);
        return ResponseEntity.ok(homestay);
    }

    @GetMapping("/structure/{id}")
    public ResponseEntity<Optional<Structure>> getStructureById(@PathVariable int id){
        Optional<Structure> structures = homestayService.getByIdStructure(id);
        return ResponseEntity.ok(structures);
    }

    @GetMapping("/structure")
    public ResponseEntity<List<Structure>> getAllStructure(){
        List<Structure> structures= homestayService.getStructureAll();
        return ResponseEntity.ok(structures);
    }


    @DeleteMapping("/structure/{id}")
    public void deleteStructure(@PathVariable int id) {
        homestayService.deleteStructure(id);
    }



    @PostMapping("/amenities")
    public ResponseEntity<Amenities> createAmenities(@RequestBody Amenities amenities) {
        Amenities homestay = homestayService.createAmenities(amenities);
        return ResponseEntity.ok(homestay);
    }

    @GetMapping("/amenities/{id}")
    public ResponseEntity<Optional<Amenities>> getAmenitiesById(@PathVariable int id){
        Optional<Amenities> amenities = homestayService.getByIdAmenities(id);
        return ResponseEntity.ok(amenities);
    }

    @GetMapping("/amenitiess")
    public ResponseEntity<List<Amenities>> getAmenitiesByIds(@RequestParam List<Integer> ids) {
        List<Amenities> amenities = homestayService.getByIdsAmenities(ids);
        return ResponseEntity.ok(amenities);
    }
    @GetMapping("/amenities")
    public ResponseEntity<List<Amenities>> getAllAmenities(){
        List<Amenities> amenities= homestayService.getAmenitiesAll();
        return ResponseEntity.ok(amenities);
    }


    @DeleteMapping("/amenities/{id}")
    public void deleteAmenities(@PathVariable int id) {
        try{
            homestayService.deleteStructure(id);
    } catch (
    DataIntegrityViolationException ex) {
            System.out.println(ex);
    }

    }

    @GetMapping("/user/{id}")
    public ResponseEntity<List<HomestayDTO>> getAllHomestaysByIdUser(@PathVariable int id){
        List<HomestayDTO> homestay = homestayService.getAllByIdUser(id);
        return ResponseEntity.ok(homestay);
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

}