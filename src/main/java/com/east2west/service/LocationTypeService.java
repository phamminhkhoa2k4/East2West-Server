package com.east2west.service;


import com.east2west.models.Entity.LocationType;
import com.east2west.models.Entity.Make;
import com.east2west.repository.LocationTypeRepository;
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
public class LocationTypeService {
    private final LocationTypeRepository locationTypeRepository;


    public LocationTypeService(LocationTypeRepository locationTypeRepository) {
        this.locationTypeRepository = locationTypeRepository;
    }

    public LocationType updateLocationType(LocationType locationType){
        Optional<LocationType> locationTypes =  locationTypeRepository.findById(locationType.getLocationtypeid());
        if(locationTypes.isPresent()){
            return locationTypeRepository.save(locationType);
        }

        return null;

    }
    public Page<LocationType> getAllLocationTypes(Pageable pageable) {
        return locationTypeRepository.findAll(pageable);
    }
    public Optional<LocationType>  getLocationTypeById(int id){
        return locationTypeRepository.findById(id);
    }

    public List<LocationType> searchLocationType(String keyword) {
        return locationTypeRepository.searchByKeyword(keyword);
    }

    public String saveLocationFromCSV(MultipartFile[] files) {
        if (files.length == 0) {
            return "Chưa chọn file!";
        }

        try {
            List<LocationType> locationTypesList = new ArrayList<>();
            Optional<LocationType> maxIdLocationType = locationTypeRepository.findAll().stream()
                    .max(Comparator.comparingInt(LocationType::getLocationtypeid));
            int idCounter = maxIdLocationType.map(locationType -> locationType.getLocationtypeid() + 1)
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


                    String LocationName = data[0].trim();
                    String LocationDescription = data[1].trim();
                    System.out.println("Đang xử lý tiện ích: " + LocationName);

                    LocationType locationType = new LocationType();
                    locationType.setLocationtypeid(idCounter++);
                    locationType.setLocationtypename(LocationName);
                    locationType.setLocationtypedescription(LocationDescription);
                    locationTypesList.add(locationType);
                }
            }


            locationTypeRepository.saveAll(locationTypesList);
            return "" + locationTypesList.size();
        } catch (Exception e) {
            return "Lỗi xử lý file: " + e.getMessage();
        }
    }


    public String deleteLocationType(int id){

        Optional<LocationType> locationType = locationTypeRepository.findById(id);
        if(locationType.isPresent()){
            locationTypeRepository.deleteById(id);
            return "Deleted " + locationType.get().getLocationtypename() + " location type successfully";
        }else{
            return "Not location type";
        }
    }


    public  Optional<LocationType> findByLocationTypeName(String LocationTypeName){
        return locationTypeRepository.findByLocationtypename(LocationTypeName);
    }


    public LocationType saveLocationType(LocationType locationType) {

        return locationTypeRepository.save(locationType);
    }

}
