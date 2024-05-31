package com.example.myapi;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.sql.*;
import java.util.ArrayList;

@Controller
public class table_list_contrllor {
    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Value("${spring.datasource.url}")
    private String url;

    @GetMapping("/files")
    public String main(Model model) {


        ArrayList<String> list=new ArrayList<>();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection myConn = DriverManager.getConnection(url, username, password);
            Statement stmt = myConn.createStatement();
            ResultSet rs = stmt.executeQuery("show tables");
            int i = 0;
            while (rs.next()) {
                list.add(rs.getString(1));
                i++;
            }
            //String[] test=list.toArray(new String[0]);
            //System.out.println(test[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        model.addAttribute("list",list);

        String list1="hello world";
        model.addAttribute("list1",list1);

        return "tablenames";
    }
}
