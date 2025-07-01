package com.east2west.service;  


import com.east2west.models.DTO.ThemeDTO;
import com.east2west.models.Entity.Theme;
import com.east2west.models.mapper.ThemeMapper;
import com.east2west.repository.ThemeRepository;
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
public class ThemeService {
    private final ThemeRepository  themeRepository;

    public ThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    public Optional<Theme> findByThemeName(String themeName){
        return themeRepository.findByThemeName(themeName);
    }

    public ThemeDTO createTheme(ThemeDTO theme) {
        return ThemeMapper.INSTANCE.toDTO(themeRepository.save(
                Theme.builder()
                        .themeId(theme.getThemeId())
                        .themeName(theme.getThemeName())
                        .build()
        ));
    }


    public String deleteTheme(int id){

        Optional<Theme> themes = themeRepository.findById(id);
        if(themes.isPresent()){
            themeRepository.deleteById(id);
            return "Deleted " + themes.get().getThemeName() + " theme successfully";
        }else{
            return "Not found theme";
        }
    }

    public Page<ThemeDTO> getAllThemes(Pageable pageable) {
        Page<Theme> themePage = themeRepository.findAll(pageable);
        return themePage.map(ThemeMapper.INSTANCE::toDTO);
    }

    public List<ThemeDTO> searchTheme(String keyword) {
        List<Theme> themeList = themeRepository.findByThemeNameContainingIgnoreCase(keyword);
        return themeList.stream().map(ThemeMapper.INSTANCE::toDTO).toList();
    }


    public Optional<ThemeDTO> getThemeById(int id) {
        Optional<Theme> theme = themeRepository.findById(id);
        if (theme.isPresent()){
            ThemeDTO themeDTO  = ThemeMapper.INSTANCE.toDTO(theme.get());
            return Optional.ofNullable(themeDTO);
        }
        return Optional.empty();
    }


    public ThemeDTO updateTheme(ThemeDTO theme){
        Optional<Theme> themes =  themeRepository.findById(theme.getThemeId());
        if(themes.isPresent()){
            Theme data = ThemeMapper.INSTANCE.toEntity(theme);
            return ThemeMapper.INSTANCE.toDTO(themeRepository.save(data));
        }
        return null;
    }



    public String saveThemeFromCSV(@NotNull MultipartFile[] files) {
        if (files.length == 0) {
            return "Chưa chọn file!";
        }

        try {
            List<Theme> themesList = new ArrayList<>();
            Optional<Theme> maxIdTheme = themeRepository.findAll().stream()
                    .max(Comparator.comparingInt(Theme::getThemeId));
            int idCounter = maxIdTheme.map(theme -> theme.getThemeId() + 1)
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



                    String ThemeName = data[0].trim();
                    System.out.println("Đang xử lý tiện ích: " + ThemeName);

                    Theme theme = new Theme();
                    theme.setThemeId(idCounter++);
                    theme.setThemeName(ThemeName);
                    themesList.add(theme);
                }
            }


            themeRepository.saveAll(themesList);
            return "" + themesList.size();
        } catch (Exception e) {
            return "Lỗi xử lý file: " + e.getMessage();
        }
    }


    public List<ThemeDTO> getAllTheme() {
        return  themeRepository.findAll().stream().map(ThemeMapper.INSTANCE::toDTO).toList();
    }
}
