package com.east2west.util;


import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.Arrays;
import java.util.List;

@Converter
public class StringListConverter implements AttributeConverter<List<String>, String> {

    @Override
    public String convertToDatabaseColumn(List<String> attribute) {
        if (attribute == null || attribute.isEmpty()) {
            return "{}";
        }
        // Ghép các phần tử và thêm dấu ngoặc nhọn
        return "{" + String.join(",", attribute) + "}";
    }

    @Override
    public List<String> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty() || dbData.equals("{}")) {
            return List.of();
        }
        String cleanedData = dbData.substring(1, dbData.length() - 1);
        return Arrays.asList(cleanedData.split(","));
    }
}



