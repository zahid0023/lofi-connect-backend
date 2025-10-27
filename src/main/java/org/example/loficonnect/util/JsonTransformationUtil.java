package org.example.loficonnect.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;

public class JsonTransformationUtil {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static <T> T getValue(JsonNode jsonNode, String fieldName, Class<T> type) {
        if (jsonNode == null || !jsonNode.has(fieldName) || jsonNode.get(fieldName).isNull()) {
            return null;
        }
        JsonNode node = jsonNode.get(fieldName);
        return mapper.convertValue(node, type);
    }

    public static <T> List<T> getList(JsonNode jsonNode, String fieldName, Class<T> elementType) {
        if (jsonNode == null || !jsonNode.has(fieldName) || jsonNode.get(fieldName).isNull()) {
            return null;
        }
        JsonNode node = jsonNode.get(fieldName);
        return mapper.convertValue(node, mapper.getTypeFactory().constructCollectionType(List.class, elementType));
    }
}
