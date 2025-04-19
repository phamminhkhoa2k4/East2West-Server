package com.east2west.service;

import com.east2west.models.DTO.ModelDTO;
import com.east2west.models.Entity.Make;
import com.east2west.models.Entity.Model;
import com.east2west.models.mapper.ModelMapper;
import com.east2west.repository.MakeRepository;
import com.east2west.repository.ModelRepository;
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
public class ModelService {

    private final ModelRepository modelRepository;

    private final MakeRepository makeRepository;

    public ModelService(ModelRepository modelRepository, MakeRepository makeRepository) {
        this.modelRepository = modelRepository;
        this.makeRepository = makeRepository;
    }

    public ModelDTO createModel(ModelDTO model) {
        Optional<Make> make  = makeRepository.findById(model.getMake().getMakeid());
        return ModelMapper.INSTANCE.toDTO(
                make.map(value -> modelRepository.save(
                        Model.builder()
                                .modelname(model.getModelname()).
                                make(value)
                                .build()
                )).orElse(null)
        );
    }


    public  Optional<Model> findByMakeIdAndModelName(int makeId, String modelName){
        return modelRepository.findByMake_MakeidAndModelname(makeId,modelName);
    }

    public List<ModelDTO> findAllModelByMakeId(int makeId){
        List<Model> modelList  = modelRepository.findByMake_Makeid(makeId);
        return  modelList.stream().map(ModelMapper.INSTANCE::toDTO).toList();
    }

    public Page<ModelDTO> getAllModels(Pageable pageable) {
        Page<Model> modelPage = modelRepository.findAll(pageable);
        return modelPage.map(ModelMapper.INSTANCE::toDTO);
    }

    public List<ModelDTO> searchModel(String keyword) {
        List<Model> modelList = modelRepository.searchByKeyword(keyword);
        return modelList.stream().map(ModelMapper.INSTANCE::toDTO).toList();
    }

    public ModelDTO updateModel(Model model){
        Optional<Model> models =  modelRepository.findById(model.getModelid());
        if(models.isEmpty()) return null;
        Optional<Make> make = makeRepository.findById(model.getMake().getMakeid());
        if (make.isEmpty()) return null;
        model.setMake(make.get());
        Model response =  modelRepository.save(model);
        return ModelMapper.INSTANCE.toDTO(response);
    }

    public String deleteModel(int id){
        Optional<Model> model = modelRepository.findById(id);
        if(model.isEmpty()) return "Not found model";
        modelRepository.deleteById(id);
        return "Deleted " + model.get().getModelname() + " model successfully  !!!";

    }

    public Optional<ModelDTO>  getModelById(int id){
        Optional<Model> model = modelRepository.findById(id);
        if(model.isEmpty()) return Optional.empty();
        ModelDTO modelDTO = ModelMapper.INSTANCE.toDTO(model.get());
        return Optional.ofNullable(modelDTO);
    }

    public String saveModelFromCSV(MultipartFile[] files) {
        if (files.length == 0) {
            return "Chưa chọn file!";
        }

        try {
            List<Model> modlesList = new ArrayList<>();
            Optional<Model> maxIdModel = modelRepository.findAll().stream()
                    .max(Comparator.comparingInt(Model::getModelid));
            int idCounter = maxIdModel.map(model -> model.getModelid() + 1)
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
                    String ModelName = data[1].trim();

                    Optional<Make> make = makeRepository.findByMakename(MakeName);

                    if(make.isEmpty()){
                        continue;
                    }
                    System.out.println("Đang xử lý tiện ích: " + ModelName);

                    Model model = new Model();
                    model.setModelid(idCounter++);
                    model.setModelname(ModelName);
                    model.setMake(make.get());
                    modlesList.add(model);
                }
            }


            modelRepository.saveAll(modlesList);
            return "" + modlesList.size();
        } catch (Exception e) {
            return "Lỗi xử lý file: " + e.getMessage();
        }
    }

}
