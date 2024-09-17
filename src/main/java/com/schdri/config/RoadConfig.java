package com.schdri.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class RoadConfig {
    @JsonProperty("roadClasses")
    private List<RoadClass> roadClasses;

    @JsonProperty("superelevationLimits")
    private List<SuperelevationLimit> superelevationLimits;

    @Data
    public static class RoadClass {
        @JsonProperty("name")
        private String name;

        @JsonProperty("speeds")
        private List<Integer> speeds;

        // Getters and setters
    }

    @Data
    public static class SuperelevationLimit {
        @JsonProperty("roadTypes")
        private List<String> roadTypes;

        @JsonProperty("regions")
        private List<RegionLimit> regions;

        // Getters and setters
    }

    @Data
    public static class RegionLimit {
        @JsonProperty("name")
        private String name;

        @JsonProperty("value")
        private int value;

        @JsonProperty("note")
        private String note;

        // Getters and setters
    }

    public static RoadConfig load() {
        ObjectMapper mapper = new ObjectMapper();
        try (InputStream is = RoadConfig.class.getResourceAsStream("/com/schdri/json/road_config.json")) {
            return mapper.readValue(is, RoadConfig.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<String> getRoadClassNames() {
        return roadClasses.stream().map(RoadClass::getName).collect(Collectors.toList());
    }

    public List<Integer> getSpeedsForRoadClass(String roadClassName) {
        return roadClasses.stream()
                .filter(rc -> rc.getName().equals(roadClassName))
                .findFirst()
                .map(RoadClass::getSpeeds)
                .orElse(List.of());
    }

    public int getSuperelevationLimit(String roadType, String region) {
        for (SuperelevationLimit limit : superelevationLimits) {
            if (limit.getRoadTypes().contains(roadType)) {
                for (RegionLimit regionLimit : limit.getRegions()) {
                    if (regionLimit.getName().equals(region)) {
                        return regionLimit.getValue();
                    }
                }
            }
        }
        throw new IllegalArgumentException("No matching superelevation limit found");
    }

    public String getSuperelevationNote(String roadType, String region) {
        for (SuperelevationLimit limit : superelevationLimits) {
            if (limit.getRoadTypes().contains(roadType)) {
                for (RegionLimit regionLimit : limit.getRegions()) {
                    if (regionLimit.getName().equals(region)) {
                        return regionLimit.getNote();
                    }
                }
            }
        }
        return null;
    }
}