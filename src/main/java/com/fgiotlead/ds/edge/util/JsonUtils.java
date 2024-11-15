package com.fgiotlead.ds.edge.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.util.List;
import java.util.Map;

public class JsonUtils {

    static ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    public static JsonNode stringToJsonNode(String jsonString) throws JsonProcessingException {
        return objectMapper.readTree(jsonString);
    }

    public static JsonNode mapToJsonNode(Map<?, ?> jsonMap) throws JsonProcessingException {
        return objectMapper.valueToTree(jsonMap);
    }

    public static Object jsonNodeToObject(JsonNode jsonNode, Class<?> type) throws JsonProcessingException {
        return objectMapper.treeToValue(jsonNode, type);
    }

    public static String listToJsonNode(List<?> jsonList) throws JsonProcessingException {
        return objectMapper.writeValueAsString(jsonList);
    }

    public static Map<?, ?> stringToMap(String jsonString) throws JsonProcessingException {
        return objectMapper.readValue(jsonString, Map.class);
    }
}
