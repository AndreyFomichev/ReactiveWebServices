package io.rest;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

@Component
//@DataSourceDefinition(name = "warehouse", className = "org.sqlite.JDBC", url = "jdbc:sqlite:Warehouse.s3db")
//@EntityScan(basePackageClasses = { WareHouseHandler.class, Jsr310JpaConverters.class })
//@EnableJpaRepositories("io.rest.jpa.repository")
public class WareHouseHandler  {

    public Mono<ServerResponse> warehouse(ServerRequest request)  {
        System.out.println("WareHouseHandler started");
        Conn conn;
        ResultSet resSet;
        Statement statmt;
        JSONArray jArray = new JSONArray();

        String categoryId = request.queryParam("categoryId").get();
        String productId = request.queryParam("productId").get();
        try {
            conn = new Conn("jdbc:sqlite:warehouse.s3db");
            Connection db = conn.getConn();

            statmt = db.createStatement();
            String sql = "SELECT * FROM CATEGORIES where id=" + categoryId
                    //" and datetime('now', 'localtime') between startonutc and expiresonutc "
                     ;
            resSet = statmt.executeQuery(sql);

            while (resSet.next()) {
                int id = resSet.getInt("id");
                String name = resSet.getString("name");
                int parentcategoryid = resSet.getInt("parentcategoryid");
                String description = resSet.getString("description");
                // System.out.println("ID = " + id);
                // System.out.println("rate = " + rate);
                JSONObject jObj = new JSONObject();
                jObj.put("type", "CATEGORIES");
                jObj.put("id", id);
                jObj.put("name", name);
                jObj.put("parentcategoryid", parentcategoryid);
                jObj.put("description", description);
                jArray.put(jObj);
            }

            sql = "SELECT * FROM PRODUCTS where id=" + productId
                    //" and datetime('now', 'localtime') between startonutc and expiresonutc "
             ;
            resSet = statmt.executeQuery(sql);

            while (resSet.next()) {

                int id = resSet.getInt("id");
                String title = resSet.getString("Title");
                String description =  resSet.getString("Description");
                int  catId = resSet.getInt("CategoryId");
                Float price = resSet.getFloat("price");
                int modelNumber= resSet.getInt("modelnumber");

                JSONObject jObj = new JSONObject();
                jObj.put("type", "PRODUCT_DISCOUNTS");
                jObj.put("Title", title);
                jObj.put("Description", description);
                jObj.put("CategoryId", catId);
                jObj.put("Price", price);
                jObj.put("ModelNumber", modelNumber);
                jArray.put(jObj);
            }

        } catch(Exception e ) {
            System.out.println("WareHouseHandler error: " + e.getMessage());
            return ServerResponse.badRequest().contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue(e.getMessage()));
        }
        System.out.println("WareHouseHandler finished, result=" + jArray.toString());

        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(jArray.toString()));
    }

}
