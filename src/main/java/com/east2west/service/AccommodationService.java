package com.east2west.service;


import com.east2west.models.DTO.AccommodationDTO;
import com.east2west.models.Entity.Accommodation;
import com.east2west.models.mapper.AccommodationMapper;
import com.east2west.repository.AccommodationRepository;
import org.jetbrains.annotations.NotNull;
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
public class AccommodationService {

    private final AccommodationRepository accommodationRepository;

    @Autowired
    public AccommodationService(AccommodationRepository accommodationRepository) {
        this.accommodationRepository = accommodationRepository;
    }


    public Optional<Accommodation> findByAccommodationName(String accommodationName){
        return accommodationRepository.findByAccommodationname(accommodationName);
    }

    public AccommodationDTO createAccommodation(AccommodationDTO accommodationDto ) {
        return AccommodationMapper.INSTANCE.toDTO(accommodationRepository.save(
                Accommodation.builder()
                        .accommodationid(accommodationDto.getAccommodationid())
                        .accommodationname(accommodationDto.getAccommodationname())
                        .accommodationthumbnail(accommodationDto.getAccommodationthumbnail())
                        .durationaccommodation(accommodationDto.getDurationaccommodation())
                        .roomtype(accommodationDto.getRoomtype())
                        .isbreakfast(accommodationDto.isIsbreakfast())
                        .accommodationtype(accommodationDto.getAccommodationtype())
                        .build()
        ));
    }


    public String deleteAccommodation(int id){

        Optional<Accommodation> accommodation = accommodationRepository.findById(id);
        if(accommodation.isPresent()){
            accommodationRepository.deleteById(id);
            return "Deleted " + accommodation.get().getAccommodationname() + " accommodation successfully";
        }else{
            return "Not found accommodation";
        }
    }

    public Page<AccommodationDTO> getAllAccommodations(Pageable pageable) {
        Page<Accommodation> accommodationPage = accommodationRepository.findAll(pageable);
        return accommodationPage.map(AccommodationMapper.INSTANCE::toDTO);
    }

    public List<AccommodationDTO> searchAccommodation(String keyword) {
        List<Accommodation> accommodationList = accommodationRepository.findByAccommodationnameContainingIgnoreCase(keyword);
        return accommodationList.stream().map(AccommodationMapper.INSTANCE::toDTO).toList();
    }


    public Optional<AccommodationDTO> getAccommodationById(int id) {
        Optional<Accommodation> accommodation = accommodationRepository.findById(id);
        if (accommodation.isPresent()){
            AccommodationDTO accommodationDTO  = AccommodationMapper.INSTANCE.toDTO(accommodation.get());
            return Optional.ofNullable(accommodationDTO);
        }
        return Optional.empty();
    }


    public AccommodationDTO updateAccommodation(AccommodationDTO accommodation){
        Optional<Accommodation> accommodations =  accommodationRepository.findById(accommodation.getAccommodationid());
        if(accommodations.isPresent()){
            Accommodation data = AccommodationMapper.INSTANCE.toEntity(accommodation);
            return AccommodationMapper.INSTANCE.toDTO(accommodationRepository.save(data));
        }
        return null;
    }



    public String saveAccommodationFromCSV(@NotNull MultipartFile[] files) {
        if (files.length == 0) {
            return "Chưa chọn file!";
        }

        try {
            List<Accommodation> accommodationsList = new ArrayList<>();
            Optional<Accommodation> maxIdAccommodation = accommodationRepository.findAll().stream()
                    .max(Comparator.comparingInt(Accommodation::getAccommodationid));
            int idCounter = maxIdAccommodation.map(accommodation -> accommodation.getAccommodationid() + 1)
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
                    if (data.length < 6) {
                        continue;
                    }



                    String AccommodationThumbnail = data[0].trim();
                    String AccommodationName = data[1].trim();
                    String AccommodationType = data[2].trim();
                    String AccommodationRoomType = data[3].trim();
                    String AccommodationDuration = data[4].trim();
                    boolean AccommodationIsBreakfast = Boolean.parseBoolean(data[5].trim());

                    System.out.println("Đang xử lý tiện ích: " + AccommodationName);

                    Accommodation accommodation = new Accommodation();
                    accommodation.setAccommodationid(idCounter++);
                    accommodation.setAccommodationname(AccommodationName);
                    accommodation.setAccommodationthumbnail(AccommodationThumbnail);
                    accommodation.setAccommodationtype(AccommodationType);
                    accommodation.setRoomtype(AccommodationRoomType);
                    accommodation.setIsbreakfast(AccommodationIsBreakfast);
                    accommodation.setDurationaccommodation(AccommodationDuration);
                    accommodationsList.add(accommodation);
                }
            }


            accommodationRepository.saveAll(accommodationsList);
            return "" + accommodationsList.size();
        } catch (Exception e) {
            return "Lỗi xử lý file: " + e.getMessage();
        }
    }

}
