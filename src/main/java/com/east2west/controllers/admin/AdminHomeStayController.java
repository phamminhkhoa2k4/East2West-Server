
package com.east2west.controllers.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.east2west.models.DTO.HomestayDTO;
import com.east2west.models.Entity.Homestay;
import com.east2west.service.HomestayService;

/**
 * AdminHomeStayController
 */
public class AdminHomeStayController {
    @Autowired
    private HomestayService homestayService;
    @PostMapping("/save")
    public ResponseEntity<Homestay> saveHomestay(@RequestBody HomestayDTO homestayDTO) {
        Homestay savedHomestay = homestayService.createOrUpdateHomestay(homestayDTO);
        return ResponseEntity.ok(savedHomestay);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHomestay(@PathVariable int id) {
        homestayService.deleteHomestay(id);
        return ResponseEntity.noContent().build();
    }
}