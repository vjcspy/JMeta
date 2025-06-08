package com.vjcspy.stockinfo.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class JsonNodeConverter implements AttributeConverter<JsonNode, Object> {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(JsonNode attribute) {
        if (attribute == null) return null;
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting JsonNode to String", e);
        }
    }


    // Xử lý mọi trường hợp kiểu dữ liệu
    @Override
    public JsonNode convertToEntityAttribute(Object dbData) {
        if (dbData == null) return null;
        try {
            if (dbData instanceof String str) {
                return objectMapper.readTree(str);
            } else if (dbData instanceof io.vertx.core.json.JsonObject jsonObject) {
                return objectMapper.readTree(jsonObject.encode());
            } else if (dbData instanceof io.vertx.core.json.JsonArray jsonArray) {
                return objectMapper.readTree(jsonArray.encode());
            } else {
                return objectMapper.readTree(dbData.toString());
            }
        } catch (Exception e) {
            throw new RuntimeException("Error converting dbData to JsonNode", e);
        }
    }
}
