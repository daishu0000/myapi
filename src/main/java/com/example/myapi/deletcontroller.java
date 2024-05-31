package com.example.myapi;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;


@RestController
public class deletcontroller {
    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Value("${spring.datasource.url}")
    private String url;

    @PostMapping("/delete")
    public String delete(@RequestBody String data) {
        try {
            // 加载MySQL JDBC驱动
            Class.forName("com.mysql.cj.jdbc.Driver");
            // 建立数据库连接
            Connection conn = DriverManager.getConnection(url, username, password);
            // 创建Statement对象执行SQL语句
            Statement stmt = conn.createStatement();


            // 执行查询并获取结果
            String delete_sqlsentance=String.format("DROP TABLE %s;",data);
            stmt.execute(delete_sqlsentance);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }
}
