package com.east2west.service;


import com.east2west.models.Entity.Amenities;
import com.east2west.models.Entity.Structure;
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


    public Structure createStructure(Structure structure) {
        return structureRepository.save(structure);
    }
    public Structure updateStructure(Structure structure) {
        Optional<Structure> struct =  structureRepository.findById(structure.getStructureid());
        if(struct.isPresent()){
            return structureRepository.save(structure);
        }
        return null;
    }

    public Optional<Structure> getByIdStructure(int id){
        return structureRepository.findById(id);
    }

    public List<Structure> getStructureAll(){
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

    public List<Structure> searchStructure(String keyword) {
        return structureRepository.findByStructurenameContainingIgnoreCase(keyword);
    }

    public Page<Structure> getAllStructure(Pageable pageable) {
        return structureRepository.findAll(pageable);
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
                    if (data.length < 1) {
                        continue;
                    }


                    String structureName = data[0].trim();
                    System.out.println("Đang xử lý : " + structureName);

                    Structure structure = new Structure();
                    structure.setStructureid(idCounter++);
                    structure.setStructurename(structureName);
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
