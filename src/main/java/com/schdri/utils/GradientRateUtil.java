package com.schdri.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;

public class GradientRateUtil {

    private static JsonNode gradientRateJson;

    static {
        try {
            ObjectMapper mapper = new ObjectMapper();
            InputStream inputStream = GradientRateUtil.class.getResourceAsStream("/com/schdri/json/gradient .json");
            JsonNode rootNode = mapper.readTree(inputStream);
            gradientRateJson = rootNode.get("gradient_rate");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getGradientRate(String lineType, String speed) {
        if (gradientRateJson == null) {
            throw new IllegalStateException("Gradient rate JSON not initialized");
        }

        JsonNode lineNode = gradientRateJson.get(lineType);
        if (lineNode == null) {
            return null; // 或者抛出异常，取决于您的错误处理策略
        }

        JsonNode rateNode = lineNode.get(speed);
        if (rateNode == null) {
            return null; // 或者抛出异常，取决于您的错误处理策略
        }

        return rateNode.asText();
    }
}
