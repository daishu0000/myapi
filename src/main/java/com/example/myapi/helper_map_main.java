package com.example.myapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;


@Controller
public class helper_map_main {
    @Autowired
    private helper_map_GeoJsonService geoJsonService;

    @GetMapping("/helper")
    public String redirectToMap() {
        return "redirect:/helper/map/100000";
    }
    
    @GetMapping("/helper/map/{fileName}")
    public String getGeoJsonPage(@PathVariable String fileName, Model model) {
        try {
            System.out.println(fileName);
            String geoJsonContent = geoJsonService.getGeoJsonContent(fileName);
            model.addAttribute("geoJsonContent", geoJsonContent);
            model.addAttribute("fileName", fileName);
            return "geojson";
        } catch (IOException e) {
            e.printStackTrace();
            return "error";
        }
    }
}
