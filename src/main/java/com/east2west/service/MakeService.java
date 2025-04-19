package com.east2west.service;


import com.east2west.models.DTO.MakeDTO;
import com.east2west.models.Entity.Make;
import com.east2west.models.mapper.MakeMapper;
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

    public MakeDTO updateMake(MakeDTO make){
        Optional<Make> makes =  makeRepository.findById(make.getMakeid());
        if(makes.isEmpty()) return null;
        Make data = MakeMapper.INSTANCE.toEntity(make);
        Make response = makeRepository.save(data);
        return MakeMapper.INSTANCE.toDTO(response);



    }
    public Page<MakeDTO> getAllMakes(Pageable pageable) {
        Page<Make> makePage = makeRepository.findAll(pageable);
        return makePage.map(MakeMapper.INSTANCE::toDTO);
    }
    public Optional<MakeDTO>  getMakeById(int id){
        Optional<Make> make = makeRepository.findById(id);
        if(make.isEmpty()) return Optional.empty();
        MakeDTO response = MakeMapper.INSTANCE.toDTO(make.get());
        return Optional.ofNullable(response);


    }

    public List<MakeDTO> searchMake(String keyword) {
        List<Make> makeList = makeRepository.findByMakenameContainingIgnoreCase(keyword);
        return makeList.stream().map(MakeMapper.INSTANCE::toDTO).toList();
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


    public List<MakeDTO> getAllMake() {
        return makeRepository.findAll().stream().map(MakeMapper.INSTANCE::toDTO).toList();
    }


    public  Optional<Make> findByMakeName(String makeName){
        return makeRepository.findByMakename(makeName);
    }


    public MakeDTO createMake(MakeDTO make) {
        Make response = makeRepository.save(MakeMapper.INSTANCE.toEntity(make));
        return MakeMapper.INSTANCE.toDTO(response);
    }

}
