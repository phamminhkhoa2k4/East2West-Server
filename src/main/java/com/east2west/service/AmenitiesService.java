package com.east2west.service;

import com.east2west.models.Entity.Amenities;
import com.east2west.repository.AmenitiesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class AmenitiesService {

    @Autowired
    private AmenitiesRepository amenitiesRepository;
    public Page<Amenities> getAllAmenities(Pageable pageable) {
        return amenitiesRepository.findAll(pageable);
    }

    public Amenities updateAmenities(Amenities amenities){
       Optional<Amenities> amenity =  amenitiesRepository.findById(amenities.getAmenitiesid());
        if(amenity.isPresent()){
            return amenitiesRepository.save(amenities);
        }

        return null;

    }


    public Amenities createAmenities(Amenities amenities) {
        return amenitiesRepository.save(amenities);
    }

    public String saveAmenitiesFromCSV(MultipartFile[] files) {
        if (files.length == 0) {
            return "Chưa chọn file!";
        }

        try {
            List<Amenities> amenitiesList = new ArrayList<>();
            Optional<Amenities> maxIdAmenity = amenitiesRepository.findAll().stream()
                    .max(Comparator.comparingInt(Amenities::getAmenitiesid));
            int idCounter = maxIdAmenity.map(amenity -> amenity.getAmenitiesid() + 1)
                    .orElse(1);

            for (MultipartFile file : files) {
                if (file.isEmpty()) {
                    continue;
                }

                BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));
                String line;
                boolean isFirstLine = true;

                while ((line = reader.readLine()) != null) {
                    if (isFirstLine) {
                        isFirstLine = false;
                        continue;
                    }

                    String[] data = line.split(",");
                    if (data.length < 1) {
                        continue;
                    }


                    String amenitiesName = data[0].trim();
                    System.out.println("Đang xử lý tiện ích: " + amenitiesName);

                    Amenities amenities = new Amenities();
                    amenities.setAmenitiesid(idCounter++);
                    amenities.setAmenitiesname(amenitiesName);
                    amenitiesList.add(amenities);
                }
            }


            amenitiesRepository.saveAll(amenitiesList);
            return "" + amenitiesList.size();
        } catch (Exception e) {
            return "Lỗi xử lý file: " + e.getMessage();
        }
    }

    public List<Amenities> searchAmenities(String keyword) {
        return amenitiesRepository.findByAmenitiesnameContainingIgnoreCase(keyword);
    }

    public Optional<Amenities>  getByIdAmenities(int id){
        return amenitiesRepository.findById(id);
    }

    public List<Amenities> getAmenitiesAll(){return  amenitiesRepository.findAll();}

    public List<Amenities> getByIdsAmenities(List<Integer> ids) {
        return amenitiesRepository.findAllById(ids);
    }
    public String deleteAmenities(int id){

          Optional<Amenities> amenities = amenitiesRepository.findById(id);
          if(amenities.isPresent()){
              amenitiesRepository.deleteById(id);
              return "Deleted " + amenities.get().getAmenitiesname() + " amenities successfully";
          }else{
              return "Not found amenities";
          }
    }
}
