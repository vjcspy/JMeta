package com.vjcspy.stockinfo.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class JsonRawConverter implements AttributeConverter<Object, Object> {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Object convertToDatabaseColumn(Object attribute) {
        // Lưu xuống db: nếu là JsonObject/JsonArray thì trả về raw, nếu là Map/List hoặc POJO thì convert sang string
        if (attribute == null) return null;
        if (attribute instanceof JsonObject || attribute instanceof JsonArray) {
            return attribute;
        }
        if (attribute instanceof String) {
            return attribute;
        }
        try {
            // Convert Map/List/POJO sang JSON string
            return objectMapper.writeValueAsString(attribute);
        } catch (Exception e) {
            throw new RuntimeException("Could not serialize attribute to JSON string", e);
        }
    }

    @Override
    public Object convertToEntityAttribute(Object dbData) {
        if (dbData == null) return null;
        if (dbData instanceof JsonObject || dbData instanceof JsonArray) {
            // Trả ra thẳng object, Quarkus sẽ tự serialize đúng khi trả về REST
            return dbData;
        }
        if (dbData instanceof String str) {
            try {
                // Thử parse string thành Map/List nếu là JSON
                if (str.trim().startsWith("[")) {
                    return objectMapper.readValue(str, java.util.List.class);
                } else if (str.trim().startsWith("{")) {
                    return objectMapper.readValue(str, java.util.Map.class);
                }
                return str;
            } catch (Exception e) {
                // Không phải JSON, trả ra nguyên string
                return str;
            }
        }
        // Nếu là kiểu lạ thì trả về toString
        return dbData.toString();
    }
}
