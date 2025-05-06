package com.east2west.service;


import com.east2west.models.DTO.SuitableDTO;
import com.east2west.models.Entity.Suitable;
import com.east2west.models.mapper.SuitableMapper;
import com.east2west.repository.SuitableRepository;
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
public class SuitableService {

    private final SuitableRepository suitableRepository;

    public SuitableService(SuitableRepository suitableRepository) {
        this.suitableRepository = suitableRepository;
    }

    public Optional<Suitable> findBySuitableName(String themeName){
        return suitableRepository.findBySuitableName(themeName);
    }

    public SuitableDTO createSuitable(SuitableDTO suitableDTO) {
        return SuitableMapper.INSTANCE.toDTO(suitableRepository.save(
                Suitable.builder()
                        .suitableId(suitableDTO.getSuitableId())
                        .suitableName(suitableDTO.getSuitableName())
                        .build()
        ));
    }


    public String deleteSuitable(int id){

        Optional<Suitable> suitable = suitableRepository.findById(id);
        if(suitable.isPresent()){
            suitableRepository.deleteById(id);
            return "Deleted " + suitable.get().getSuitableName() + " suitable successfully";
        }else{
            return "Not found suitable";
        }
    }

    public Page<SuitableDTO> getAllSuitableList(Pageable pageable) {
        Page<Suitable> themePage = suitableRepository.findAll(pageable);
        return themePage.map(SuitableMapper.INSTANCE::toDTO);
    }

    public List<SuitableDTO> searchSuitable(String keyword) {
        List<Suitable> suitableList = suitableRepository.findBySuitableNameContainingIgnoreCase(keyword);
        return suitableList.stream().map(SuitableMapper.INSTANCE::toDTO).toList();
    }


    public Optional<SuitableDTO> getSuitableById(int id) {
        Optional<Suitable> suitable = suitableRepository.findById(id);
        if (suitable.isPresent()){
            SuitableDTO suitableDTO  = SuitableMapper.INSTANCE.toDTO(suitable.get());
            return Optional.ofNullable(suitableDTO);
        }
        return Optional.empty();
    }


    public SuitableDTO updateSuitable(SuitableDTO suitableDTO){
        Optional<Suitable>  suitable =  suitableRepository.findById(suitableDTO.getSuitableId());
        if(suitable.isPresent()){
            Suitable data = SuitableMapper.INSTANCE.toEntity(suitableDTO);
            return SuitableMapper.INSTANCE.toDTO(suitableRepository.save(data));
        }
        return null;
    }



    public String saveSuitableFromCSV(@NotNull MultipartFile[] files) {
        if (files.length == 0) {
            return "Chưa chọn file!";
        }

        try {
            List<Suitable> suitableList = new ArrayList<>();
            Optional<Suitable> maxIdSuitable = suitableRepository.findAll().stream()
                    .max(Comparator.comparingInt(Suitable::getSuitableId));
            int idCounter = maxIdSuitable.map(suitable -> suitable.getSuitableId() + 1)
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



                    String SuitableName = data[0].trim();
                    System.out.println("Đang xử lý tiện ích: " + SuitableName);

                    Suitable suitable = new Suitable();
                    suitable.setSuitableId(idCounter++);
                    suitable.setSuitableName(SuitableName);
                    suitableList.add(suitable);
                }
            }


            suitableRepository.saveAll(suitableList);
            return "" + suitableList.size();
        } catch (Exception e) {
            return "Lỗi xử lý file: " + e.getMessage();
        }
    }
}
