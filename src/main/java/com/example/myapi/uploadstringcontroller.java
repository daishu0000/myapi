package com.example.myapi;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import com.google.gson.JsonElement;
import com.google.gson.Gson;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@RestController
public class uploadstringcontroller {
    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Value("${spring.datasource.url}")
    private String url;
    @PostMapping("/uploadstring")
    public String uploadstring(@RequestBody String data) {
        String geojson_name = "";
        String geojson_status = "";
        JsonObject geojson_data_obj = new JsonObject();
        JsonArray geojson_features = new JsonArray();

        try {
            Gson gson = new Gson();
            JsonElement element = gson.fromJson(data, JsonElement.class);
            JsonObject jsonObject = element.getAsJsonObject();

            JsonElement geojson_name_element = jsonObject.get("name");
            geojson_name = geojson_name_element.getAsString();
            //status为new,创建新表，为old，覆盖原表
            JsonElement geojson_ststus_element = jsonObject.get("status");
            geojson_status = geojson_ststus_element.getAsString();

            JsonElement geojson_data_element = jsonObject.get("data");
            geojson_data_obj = geojson_data_element.getAsJsonObject();
            JsonElement geojson_features_element = geojson_data_obj.get("features");
            geojson_features = geojson_features_element.getAsJsonArray();


        } catch (Exception e) {
            e.printStackTrace();
        }

        if (geojson_name.isEmpty()) {
            return "error";
        } else {
            try {
                // 加载MySQL JDBC驱动
                Class.forName("com.mysql.cj.jdbc.Driver");
                // 建立数据库连接
                Connection conn = DriverManager.getConnection(url, username, password);
                // 创建Statement对象执行SQL语句
                Statement stmt = conn.createStatement();

                String if_sqlsentance = "SHOW TABLES;";
                ResultSet rs = stmt.executeQuery(if_sqlsentance);
                boolean tableFound = false;
                while (rs.next()) {
                    String tableName = rs.getString(1);
                    if (tableName.equals(geojson_name)) {
                        tableFound = true;
                        break;
                    }
                }

                if (tableFound) {
                    if (geojson_status.equals("old")) {
                        String delete_sqlsentance=String.format("DROP TABLE %s;",geojson_name);
                        stmt.execute(delete_sqlsentance);
                    }
                    else{
                        return "表已创建";

                    }
                }
                String create_sqlsentance = String.format("""
                        CREATE TABLE `%s` (
                            `id` INT NOT NULL AUTO_INCREMENT,
                            `properties` JSON NOT NULL,
                            `geometry` GEOMETRY NOT NULL,
                            PRIMARY KEY (`id`)
                        );
                        """, geojson_name);


                stmt.execute(create_sqlsentance);

                PreparedStatement pstmt = null;
                JsonObject geojson_geometry = new JsonObject();
                JsonObject geojson_properties = new JsonObject();

                for (int i = 0; i < geojson_features.size(); i++) {
                    JsonElement geojson_feature_element = geojson_features.get(i);
                    JsonObject geojson_feature = geojson_feature_element.getAsJsonObject();

                    JsonElement geojson_geometry_element = geojson_feature.get("geometry");
                    geojson_geometry = geojson_geometry_element.getAsJsonObject();
                    JsonElement geojson_properties_element = geojson_feature.get("properties");
                    geojson_properties = geojson_properties_element.getAsJsonObject();
                    String initialize_sqlsentance = String.format("INSERT INTO %s (properties,geometry) VALUES (?,ST_GeomFromGeoJSON(?))", geojson_name);
                    pstmt = conn.prepareStatement(initialize_sqlsentance);
                    pstmt.setString(1, geojson_properties.toString());
                    pstmt.setString(2, geojson_geometry.toString());
                    pstmt.execute();
                }


                stmt.close();
                conn.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return data;

        }
    }
}
