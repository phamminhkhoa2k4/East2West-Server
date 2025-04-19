package com.east2west.service;


import com.east2west.models.DTO.StructureDTO;
import com.east2west.models.Entity.Structure;
import com.east2west.models.mapper.StructureMapper;
import com.east2west.repository.StructureRepository;
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
public class StructureService {

    @Autowired
    private StructureRepository structureRepository;


    public StructureDTO createStructure(StructureDTO structure) {
         Structure data = StructureMapper.INSTANCE.toEntity(structure);
         Structure response = structureRepository.save(data);
        return StructureMapper.INSTANCE.toDTO(response);
    }
    public StructureDTO updateStructure(StructureDTO structure) {
        Optional<Structure> struct =  structureRepository.findById(structure.getStructureid());
        if(struct.isEmpty()) return null;
        Structure data = StructureMapper.INSTANCE.toEntity(structure);
        Structure response = structureRepository.save(data);
        return StructureMapper.INSTANCE.toDTO(response);

    }

    public Optional<StructureDTO> getStructureById(int id){
        Optional<Structure> structure = structureRepository.findById(id);
        if(structure.isEmpty()) return Optional.empty();
        StructureDTO response = StructureMapper.INSTANCE.toDTO(structure.get());
        return Optional.ofNullable(response);
    }

    public List<Structure> getAllStructure(){
        return structureRepository.findAll();
    }

    public String deleteStructure(int id){
        Optional<Structure> structure = structureRepository.findById(id);
        if(structure.isPresent()){
            structureRepository.deleteById(id);
            return "Deleted " + structure.get().getStructurename() + " structure successfully";
        }else{
            return "Not found structure";
        }
    }

    public List<StructureDTO> searchStructure(String keyword) {
        List<Structure> structureList = structureRepository.findByStructurenameContainingIgnoreCase(keyword);
        return structureList.stream().map(StructureMapper.INSTANCE::toDTO).toList();
    }

    public Page<StructureDTO> getAllStructure(Pageable pageable) {
        Page<Structure> structurePage = structureRepository.findAll(pageable);
        return structurePage.map(StructureMapper.INSTANCE::toDTO);
    }

    public String saveStructureFromCSV(MultipartFile[] files) {
        if (files.length == 0) {
            return "Chưa chọn file!";
        }

        try {
            List<Structure> structureList = new ArrayList<>();
            Optional<Structure> maxIdStructure = structureRepository.findAll().stream()
                    .max(Comparator.comparingInt(Structure::getStructureid));
            int idCounter = maxIdStructure.map(structure -> structure.getStructureid() + 1)
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


                    String structureName = data[0].trim();
                    String structureIcon = data[1].trim();
                    System.out.println("Đang xử lý : " + structureName);

                    Structure structure = new Structure();
                    structure.setStructureid(idCounter++);
                    structure.setStructurename(structureName);
                    structure.setStructureicon(structureIcon);
                    structureList.add(structure);
                }
            }


            structureRepository.saveAll(structureList);
            return "" + structureList.size();
        } catch (Exception e) {
            return "Lỗi xử lý file: " + e.getMessage();
        }
    }

}
