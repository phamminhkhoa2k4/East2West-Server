package com.east2west.service;

import com.east2west.models.DTO.AmenitiesDTO;
import com.east2west.models.Entity.Amenities;
import com.east2west.models.mapper.AmenitiesMapper;
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
    public Page<AmenitiesDTO> getAllAmenities(Pageable pageable) {
        Page<Amenities> amenitiesPage =  amenitiesRepository.findAll(pageable);
        return amenitiesPage.map(AmenitiesMapper.INSTANCE::toDTO);
    }

    public AmenitiesDTO updateAmenities(AmenitiesDTO amenities){
       Optional<Amenities> amenity =  amenitiesRepository.findById(amenities.getAmenitiesid());
        if(amenity.isEmpty()) return null;
        Amenities data = AmenitiesMapper.INSTANCE.toEntity(amenities);
        Amenities response = amenitiesRepository.save(data);
        return AmenitiesMapper.INSTANCE.toDTO(response);
    }


    public AmenitiesDTO createAmenities(AmenitiesDTO amenities) {
        Amenities data = AmenitiesMapper.INSTANCE.toEntity(amenities);
        Amenities response = amenitiesRepository.save(data);
        return AmenitiesMapper.INSTANCE.toDTO(response);
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
                    if (data.length < 2) {
                        continue;
                    }


                    String amenitiesName = data[0].trim();
                    String amenitiesIcon = data[1].trim();
                    System.out.println("Đang xử lý tiện ích: " + amenitiesName);

                    Amenities amenities = new Amenities();
                    amenities.setAmenitiesid(idCounter++);
                    amenities.setAmenitiesname(amenitiesName);
                    amenities.setAmenitiesicon(amenitiesIcon);
                    amenitiesList.add(amenities);
                }
            }


            amenitiesRepository.saveAll(amenitiesList);
            return "" + amenitiesList.size();
        } catch (Exception e) {
            return "Lỗi xử lý file: " + e.getMessage();
        }
    }

    public List<AmenitiesDTO> searchAmenities(String keyword) {
        List<Amenities> amenitiesList = amenitiesRepository.findByAmenitiesnameContainingIgnoreCase(keyword);
        return amenitiesList.stream().map(AmenitiesMapper.INSTANCE::toDTO).toList();
    }

    public Optional<AmenitiesDTO>  getAmenitiesById(int id){
        Optional<Amenities> amenities = amenitiesRepository.findById(id);
        if(amenities.isEmpty()) return Optional.empty();
        AmenitiesDTO response = AmenitiesMapper.INSTANCE.toDTO(amenities.get());
        return Optional.ofNullable(response);
    }

    public List<AmenitiesDTO> getAmenitiesAll(){
        List<Amenities> amenitiesList = amenitiesRepository.findAll();
        return  amenitiesList.stream().map(AmenitiesMapper.INSTANCE::toDTO).toList();
    }

    public List<AmenitiesDTO> getByIdsAmenities(List<Integer> ids) {
        List<Amenities> amenitiesList = amenitiesRepository.findAllById(ids);
        return  amenitiesList.stream().map(AmenitiesMapper.INSTANCE::toDTO).toList();
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
