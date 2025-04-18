package com.east2west.service;

import com.east2west.models.Entity.Make;
import com.east2west.models.Entity.Model;
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

    public Model createModel(Model model) {
        Optional<Make> make  = makeRepository.findById(model.getMake().getMakeid());
        return make.map(value -> modelRepository.save(Model.builder().modelname(model.getModelname()).make(value).build())).orElse(null);
    }


    public  Optional<Model> findByMakeIdAndModelName(int makeId, String modelName){
        return modelRepository.findByMake_MakeidAndModelname(makeId,modelName);
    }

    public List<Model> findAllModelByMakeId(int makeId){
        return  modelRepository.findByMake_Makeid(makeId);
    }

    public Page<Model> getAllModels(Pageable pageable) {
        return modelRepository.findAll(pageable);
    }

    public List<Model> searchModel(String keyword) {
        return modelRepository.searchByKeyword(keyword);
    }

    public Model updateModel(Model model){
        Optional<Model> models =  modelRepository.findById(model.getModelid());
        if(models.isPresent()){
            Optional<Make> make = makeRepository.findById(model.getMake().getMakeid());
            if (make.isPresent()){
                model.setMake(make.get());
                return modelRepository.save(model);
            }
        }

        return null;

    }

    public String deleteModel(int id){

        Optional<Model> model = modelRepository.findById(id);
        if(model.isPresent()){
            modelRepository.deleteById(id);
            return "Deleted " + model.get().getModelname() + " model successfully  !!!";
        }else{
            return "Not found model";
        }
    }

    public Optional<Model>  getByIdModel(int id){
        return modelRepository.findById(id);
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
