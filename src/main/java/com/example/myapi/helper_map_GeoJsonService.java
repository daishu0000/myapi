package com.example.myapi;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import java.io.IOException;

@Service
public class helper_map_GeoJsonService {
    private static final String GEOJSON_DIR = "geojson_full";

    public String getGeoJsonContent(String fileName) throws IOException {
        ClassPathResource resource = new ClassPathResource(GEOJSON_DIR + "/" + fileName + "_full.json");

        return new String(StreamUtils.copyToByteArray(resource.getInputStream()), "UTF-8");
    }

}
