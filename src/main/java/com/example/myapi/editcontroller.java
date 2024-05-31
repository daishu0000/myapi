package com.example.myapi;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller

public class editcontroller {
    @GetMapping("/edit")
    public String edit() {
        return "edit";
    }
}
