package com.east2west.service;


import com.east2west.models.DTO.LocationTypeDTO;
import com.east2west.models.Entity.LocationType;
import com.east2west.models.mapper.LocationTypeMapper;
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

    public LocationTypeDTO updateLocationType(LocationTypeDTO locationType){
        Optional<LocationType> locationTypes =  locationTypeRepository.findById(locationType.getLocationtypeid());
        if(locationTypes.isEmpty()) return null;
        LocationType data = LocationTypeMapper.INSTANCE.toEntity(locationType);
        LocationType response = locationTypeRepository.save(data);
        return LocationTypeMapper.INSTANCE.toDTO(response);
    }
    public Page<LocationTypeDTO> getAllLocationTypes(Pageable pageable) {
        Page<LocationType> locationTypePage = locationTypeRepository.findAll(pageable);
        return locationTypePage.map(LocationTypeMapper.INSTANCE::toDTO);
    }

    public List<LocationTypeDTO> getAllLocationType() {
        List<LocationType> locationTypeList = locationTypeRepository.findAll();
        return locationTypeList.stream().map(LocationTypeMapper.INSTANCE::toDTO).toList();
    }
    public Optional<LocationTypeDTO>  getLocationTypeById(int id){
        Optional<LocationType> locationType = locationTypeRepository.findById(id);
        if(locationType.isEmpty()) return  Optional.empty();
        LocationTypeDTO response  = LocationTypeMapper.INSTANCE.toDTO(locationType.get());
        return Optional.ofNullable(response);
    }

    public List<LocationTypeDTO> searchLocationType(String keyword) {
        List<LocationType> locationTypeList = locationTypeRepository.searchByKeyword(keyword);
        return locationTypeList.stream().map(LocationTypeMapper.INSTANCE::toDTO).toList();
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


    public LocationTypeDTO createLocationType(LocationTypeDTO locationType) {
        LocationType data = LocationTypeMapper.INSTANCE.toEntity(locationType);
        return LocationTypeMapper.INSTANCE.toDTO(data);
    }

}
