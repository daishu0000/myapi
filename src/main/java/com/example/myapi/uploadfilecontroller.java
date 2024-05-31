package com.example.myapi;


import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@RestController

public class uploadfilecontroller {
    @PostMapping("/uploadfile")
    public String upload(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return "File is empty";
        }
        try {
            String filename=file.getOriginalFilename();
            String extension=filename.substring(filename.lastIndexOf(".")+1);
            if(extension.equals("geojson")||extension.equals("json")){

            }
            String content = new String(file.getBytes(), StandardCharsets.UTF_8);
            return content;
        } catch (IOException e) {
            e.printStackTrace();
            return "Failed to read file content";
        }
    }

}
