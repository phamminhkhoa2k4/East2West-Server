package com.east2west.service;


import com.east2west.models.Entity.Make;
import com.east2west.repository.MakeRepository;
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
public class MakeService {


    private final MakeRepository makeRepository;

    @Autowired
    public MakeService(MakeRepository makeRepository) {
        this.makeRepository = makeRepository;
    }

    public Make updateMake(Make make){
        Optional<Make> makes =  makeRepository.findById(make.getMakeid());
        if(makes.isPresent()){
            return makeRepository.save(make);
        }

        return null;

    }
    public Page<Make> getAllMakes(Pageable pageable) {
        return makeRepository.findAll(pageable);
    }
    public Optional<Make>  getByIdMake(int id){
        return makeRepository.findById(id);
    }

    public List<Make> searchMake(String keyword) {
        return makeRepository.findByMakenameContainingIgnoreCase(keyword);
    }

    public String saveMakeFromCSV(@NotNull MultipartFile[] files) {
        if (files.length == 0) {
            return "Chưa chọn file!";
        }

        try {
            List<Make> makesList = new ArrayList<>();
            Optional<Make> maxIdMake = makeRepository.findAll().stream()
                    .max(Comparator.comparingInt(Make::getMakeid));
            int idCounter = maxIdMake.map(make -> make.getMakeid() + 1)
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


                    String MakeName = data[0].trim();
                    String MakeLogo = data[1].trim();
                    System.out.println("Đang xử lý tiện ích: " + MakeName);

                    Make make = new Make();
                    make.setMakeid(idCounter++);
                    make.setMakename(MakeName);
                    make.setLogo(MakeLogo);
                    makesList.add(make);
                }
            }


            makeRepository.saveAll(makesList);
            return "" + makesList.size();
        } catch (Exception e) {
            return "Lỗi xử lý file: " + e.getMessage();
        }
    }


    public String deleteMake(int id){

        Optional<Make> makes = makeRepository.findById(id);
        if(makes.isPresent()){
            makeRepository.deleteById(id);
            return "Deleted " + makes.get().getMakename() + " make successfully";
        }else{
            return "Not found make";
        }
    }


    public List<Make> getAllMake() {
        return makeRepository.findAll();
    }


    public  Optional<Make> findByMakeName(String makeName){
        return makeRepository.findByMakename(makeName);
    }


    public Make saveMake(Make make) {

        return makeRepository.save(make);
    }

}
