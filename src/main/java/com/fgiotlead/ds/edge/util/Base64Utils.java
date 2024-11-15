package com.fgiotlead.ds.edge.util;

import java.util.Base64;
import java.util.List;
import java.util.Map;

public class Base64Utils {

    public static String encode(List<Map<String, Object>> state) {
        try {
            String stateJson = JsonUtils.listToJsonNode(state);
            return Base64.getEncoder().encodeToString(stateJson.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    public static String decode(String base64String) {
        return new String(Base64.getDecoder().decode(base64String));
    }
}
