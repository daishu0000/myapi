package com.example.myapi;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.sql.*;
import java.util.ArrayList;

import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


@RestController
public class table_controller {
    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Value("${spring.datasource.url}")
    private String url;

    @GetMapping("/file/{tablename}")
    public ResponseEntity<?> main(@PathVariable("tablename") String tablename) {



        //最终输出的json对象
        JsonObject result_json_obj = new JsonObject();
        result_json_obj.addProperty("type", "FeatureCollection");

        //geojson的features数组
        JsonArray geojson_features = new JsonArray();

        try {
            // 加载MySQL JDBC驱动
            Class.forName("com.mysql.cj.jdbc.Driver");
            // 建立数据库连接
            Connection conn = DriverManager.getConnection(url, username, password);
            // 创建Statement对象执行SQL语句
            Statement stmt = conn.createStatement();


            // 执行查询并获取结果
            String sql_sentance = "SELECT ST_AsGeoJSON(geometry) AS geometry , properties FROM " + tablename;

            ResultSet rs = stmt.executeQuery(sql_sentance);


            // 处理结果
            while (rs.next()) {
                //每一个feature
                JsonObject geojson_feature = new JsonObject();
                geojson_feature.addProperty("type", "Feature");

                //properties属性
                String geojson_feature_propertiesstr = rs.getString("properties");
                JsonObject geojson_feature_properties = JsonParser.parseString(geojson_feature_propertiesstr).getAsJsonObject();

                //geometry属性
                String geojson_feature_geometrystr = rs.getString("geometry");
                JsonObject geojson_feature_geometry = JsonParser.parseString(geojson_feature_geometrystr).getAsJsonObject();

                //添加到feature对象
                geojson_feature.add("geometry", geojson_feature_geometry);
                geojson_feature.add("properties", geojson_feature_properties);

                //添加到列表
                geojson_features.add(geojson_feature);

            }
            //添加到输出的json
            result_json_obj.add("features", geojson_features);

            // 关闭结果集、Statement和连接
            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(result_json_obj.toString(), HttpStatus.OK);

    }


}
