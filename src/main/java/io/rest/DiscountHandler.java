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
//@EntityScan(basePackageClasses = { DiscountHandler.class, Jsr310JpaConverters.class })
//@DataSourceDefinition(name = "discounts", className = "org.sqlite.JDBC", url = "jdbc:sqlite:Discounts.s3db")
//@EnableJpaRepositories("io.rest.jpa.repository")
public class DiscountHandler{

    public Mono<ServerResponse> discount(ServerRequest request)  {
        System.out.println("DiscountHandler started");
        Conn conn;
        ResultSet resSet;
        Statement statmt;
        JSONArray jArray = new JSONArray();

        String categoryId = request.queryParam("categoryId").get();
        String productId = request.queryParam("productId").get();
        try {
          //  Class.forName("jdbc.SQLite");
            conn = new Conn("jdbc:sqlite:Discounts.s3db");
            Connection db = conn.getConn();

            statmt = db.createStatement();
            String sql = "SELECT * FROM CATEGORY_DISCOUNTS where category_id=" + categoryId +
                    " and datetime('now', 'localtime') between startonutc and expiresonutc ";
            resSet = statmt.executeQuery(sql);

            while (resSet.next()) {
                int id = resSet.getInt("id");
                Float rate = resSet.getFloat("rate");
               // System.out.println("ID = " + id);
               // System.out.println("rate = " + rate);
                JSONObject jObj = new JSONObject();
                jObj.put("type", "CATEGORY_DISCOUNTS");
                jObj.put("id", id);
                jObj.put("rate", rate);
                jArray.put(jObj);
            }

            sql = "SELECT * FROM PRODUCT_DISCOUNTS where product_id=" + productId +
                    " and datetime('now', 'localtime') between startonutc and expiresonutc ";
            resSet = statmt.executeQuery(sql);

            while (resSet.next()) {
                int id = resSet.getInt("id");
                Float rate = resSet.getFloat("rate");
                // System.out.println("ID = " + id);
                // System.out.println("rate = " + rate);
                JSONObject jObj = new JSONObject();
                jObj.put("type", "PRODUCT_DISCOUNTS");
                jObj.put("id", id);
                jObj.put("rate", rate);
                jArray.put(jObj);
            }

    } catch(Exception e ) {
            System.out.println("DiscountHandler error: " + e.getMessage());
            return ServerResponse.badRequest().contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue(e.getMessage()));
        }
        System.out.println("DiscountHandler finished, result=" + jArray.toString());

        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(jArray.toString()));
    }
    /*

    @Override
    public DiscountsResponse handleRequest(DiscountsRequest request) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date startOnUtc, expiresOnUtc;
        Date now = new Date();

        Float productDiscount = 0F, categoryDiscount=0F, rate = 0F;
        try {

            Iterable<CategoryDiscounts> categoryDiscounts = categoryDiscountsRepository.findAll();
            Iterable<ProductDiscounts> productDiscounts = productsDiscountsRepository.findAll();

            for (CategoryDiscounts cd : categoryDiscounts) {
                startOnUtc = dateFormat.parse(cd.getStartOnUtc());
                expiresOnUtc = dateFormat.parse(cd.getExpiresOnUtc());
                if (cd.getCategoryId() == request.getCategoryId()
                     && now.after(startOnUtc)
                     && now.before(expiresOnUtc) )
                    categoryDiscount = cd.getRate();
            }

            for (ProductDiscounts pd : productDiscounts) {
                startOnUtc = dateFormat.parse(pd.getStartOnUtc());
                expiresOnUtc = dateFormat.parse(pd.getExpiresOnUtc());
                if (pd.getProductId() == request.getCategoryId()
                        && now.after(startOnUtc)
                        && now.before(expiresOnUtc) )
                    productDiscount = pd.getRate();
            }

            rate = (100.0F - (productDiscount + categoryDiscount)) / 100.0F;

            return createOkResponse(rate);
        } catch (Exception e) {
            DiscountsResponse err = createResponseWithError("ERROR", e.getMessage());
            return err;
        }
    }

    private DiscountsResponse createResponseWithError(String code, String message) {
        DiscountsResponse discountsResponse = new DiscountsResponse();
        Response response = new Response();
        response.setResult("ERROR");
        response.setErrors(new ArrayList<>());
        response.getErrors().add(new io.rest.controller.discount.model.elements.Error(code, message));

        discountsResponse.setResponse(response);
        return discountsResponse;
    }

    private DiscountsResponse createOkResponse(Float rate) {
        DiscountsResponse discountsResponse = new DiscountsResponse();
        Response response = new Response();
        response.setResult("OK");
        response.setRate(String.valueOf(rate));

        discountsResponse.setResponse(response);
        return discountsResponse;
    }
     */
}
