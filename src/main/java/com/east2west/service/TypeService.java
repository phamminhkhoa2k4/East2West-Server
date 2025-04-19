package com.east2west.service;

import com.east2west.models.DTO.TypeDTO;
import com.east2west.models.Entity.Type;
import com.east2west.models.mapper.TypeMapper;
import com.east2west.repository.TypeRepository;
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
public class TypeService {

    private final TypeRepository typeRepository;

    @Autowired
    public TypeService(TypeRepository typeRepository) {
        this.typeRepository = typeRepository;
    }

    public Optional<Type> findByTypeName(String typeName){
        return typeRepository.findByTypename(typeName);
    }

    public TypeDTO saveType(TypeDTO type) {
        Type data = TypeMapper.INSTANCE.toEntity(type);
        return TypeMapper.INSTANCE.toDTO(typeRepository.save(data));
    }


    public TypeDTO updateType (TypeDTO type){
        Optional<Type> types =  typeRepository.findById(type.getTypeid());
        if(types.isPresent()){
            Type data = TypeMapper.INSTANCE.toEntity(type);
            return TypeMapper.INSTANCE.toDTO(typeRepository.save(data));
        }
        return null;
    }

    public String deleteType(int id){

        Optional<Type> types = typeRepository.findById(id);
        if(types.isPresent()){
            typeRepository.deleteById(id);
            return "Deleted " + types.get().getTypename() + " type successfully";
        }else{
            return "Not found make";
        }
    }



    public Optional<TypeDTO> getTypeById(int id) {
        Optional<Type> type = typeRepository.findById(id);
        if (type.isPresent()){
            TypeDTO typeDTO  = TypeMapper.INSTANCE.toDTO(type.get());
            return Optional.ofNullable(typeDTO);
        }
        return Optional.empty();
    }

    public List<TypeDTO> searchType(String keyword) {
        List<Type> typeList = typeRepository.findByTypenameContainingIgnoreCase(keyword);
        return typeList.stream().map(TypeMapper.INSTANCE::toDTO).toList();
    }

    public String saveTypeFromCSV(MultipartFile[] files) {
        if (files.length == 0) {
            return "Chưa chọn file!";
        }

        try {
            List<Type> typeList = new ArrayList<>();
            Optional<Type> maxIdType = typeRepository.findAll().stream()
                    .max(Comparator.comparingInt(Type::getTypeid));
            int idCounter = maxIdType.map(type -> type.getTypeid() + 1)
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


                    String typeName = data[0].trim();
                    String typeIcon = data[1].trim();
                    System.out.println("Đang xử lý tiện ích: " + typeName);

                    Type type = new Type();
                    type.setTypeid(idCounter++);
                    type.setTypename(typeName);
                    type.setTypeicon(typeIcon);
                    typeList.add(type);
                }
            }

            typeRepository.saveAll(typeList);
            return "" + typeList.size();
        } catch (Exception e) {
            return "Lỗi xử lý file: " + e.getMessage();
        }
    }

    public Page<TypeDTO> getAllTypes(Pageable pageable) {
        Page<Type> typePage = typeRepository.findAll(pageable);
        return typePage.map(TypeMapper.INSTANCE::toDTO);
    }

    public List<TypeDTO> getAllType() {
        List<Type> typeList = typeRepository.findAll();
        return typeList.stream().map(TypeMapper.INSTANCE::toDTO).toList();
    }

}
