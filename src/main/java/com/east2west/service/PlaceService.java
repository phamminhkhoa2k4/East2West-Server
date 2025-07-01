package com.east2west.service;


import com.east2west.models.DTO.PlaceDTO;
import com.east2west.models.Entity.Place;
import com.east2west.models.mapper.PlaceMapper;
import com.east2west.repository.PlaceRepository;
import org.jetbrains.annotations.NotNull;
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
public class PlaceService {

    private final PlaceRepository placeRepository;

    public PlaceService(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
    }




    public Optional<Place> findByPlaceName(String placeName){
        return placeRepository.findByPlacename(placeName);
    }

    public PlaceDTO createPlace(PlaceDTO place) {
        return PlaceMapper.INSTANCE.toDTO(placeRepository.save(
                Place.builder()
                        .placeid(place.getPlaceid())
                        .placename(place.getPlacename())
                        .placethumbnail(place.getPlacethumbnail())
                        .placeduration(place.getPlaceduration())
                        .description(place.getDescription())
                        .build()
        ));
    }


    public String deletePlace(int id){

        Optional<Place> places = placeRepository.findById(id);
        if(places.isPresent()){
            placeRepository.deleteById(id);
            return "Deleted " + places.get().getPlacename() + " place successfully";
        }else{
            return "Not found place";
        }
    }

    public Page<PlaceDTO> getAllPlaces(Pageable pageable) {
        Page<Place> placePage = placeRepository.findAll(pageable);
        return placePage.map(PlaceMapper.INSTANCE::toDTO);
    }

    public List<PlaceDTO> searchPlace(String keyword) {
        List<Place> placeList = placeRepository.findByPlacenameContainingIgnoreCase(keyword);
        return placeList.stream().map(PlaceMapper.INSTANCE::toDTO).toList();
    }


    public Optional<PlaceDTO> getPlaceById(int id) {
        Optional<Place> place = placeRepository.findById(id);
        if (place.isPresent()){
            PlaceDTO placeDTO  = PlaceMapper.INSTANCE.toDTO(place.get());
            return Optional.ofNullable(placeDTO);
        }
        return Optional.empty();
    }


    public PlaceDTO updatePlace(PlaceDTO place){
        Optional<Place> places =  placeRepository.findById(place.getPlaceid());
        if(places.isPresent()){
            Place data = PlaceMapper.INSTANCE.toEntity(place);
            return PlaceMapper.INSTANCE.toDTO(placeRepository.save(data));
        }
        return null;
    }



    public String savePlaceFromCSV(@NotNull MultipartFile[] files) {
        if (files.length == 0) {
            return "Chưa chọn file!";
        }

        try {
            List<Place> placesList = new ArrayList<>();
            Optional<Place> maxIdPlace = placeRepository.findAll().stream()
                    .max(Comparator.comparingInt(Place::getPlaceid));
            int idCounter = maxIdPlace.map(place -> place.getPlaceid() + 1)
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
                    if (data.length < 4) {
                        continue;
                    }



                    String PlaceThumbnail = data[0].trim();
                    String PlaceName = data[1].trim();
                    String PlaceDuration = data[2].trim();
                    String PlaceDescription = data[3].trim();
                    System.out.println("Đang xử lý tiện ích: " + PlaceName);

                    Place place = new Place();
                    place.setPlaceid(idCounter++);
                    place.setPlacename(PlaceName);
                    place.setPlacethumbnail(PlaceThumbnail);
                    place.setPlaceduration(PlaceDuration);
                    place.setDescription(PlaceDescription);
                    placesList.add(place);
                }
            }


            placeRepository.saveAll(placesList);
            return "" + placesList.size();
        } catch (Exception e) {
            return "Lỗi xử lý file: " + e.getMessage();
        }
    }


    public List<PlaceDTO> getAllPlace(){
        return placeRepository.findAll().stream().map(PlaceMapper.INSTANCE::toDTO).toList();
    }
}
