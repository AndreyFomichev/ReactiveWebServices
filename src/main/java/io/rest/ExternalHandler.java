package io.rest;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class ExternalHandler {

	public Mono<ServerResponse> external(ServerRequest request) {
		//Stream<String> resDiscounts, resWarehouse
		String strDiscounts, strWarehouse;
		String discounts, warehouse;
		JSONArray jArray = new JSONArray();

		try {
			System.out.println("ExternalHandler.external started");

			String productId  = request.queryParam("productId").get();
			String categoryId = request.queryParam("categoryId").get();
			MyWebClient discountMyWebClient = new MyWebClient("/discount/", productId, categoryId);
            Thread t = new Thread (discountMyWebClient);
            t.start();
			strDiscounts = discountMyWebClient.getResult();
			System.out.println("discountWebClient finished, result=" + strDiscounts);

			MyWebClient myWebClient = new MyWebClient("/warehouse/", productId, categoryId);
			Thread t2 = new Thread (myWebClient);
			t2.start();
			strWarehouse = myWebClient.getResult();
			System.out.println("warehouseWebClient finished, result= " + strWarehouse);

			// здесь будет код для склейки результатов обоих сервисов
			//..Тогда расчет окончательной цены по формуле:
          //[Price] = [Original Price] x ((100,0 - ([Product Discount] + [Category Discount])) / 100,0)
			JSONObject jObj = new JSONObject();
			jObj.put("type", "RESULT");
			jObj.put("id", 0);
			jObj.put("name", 0);
			jArray.put(jObj);

			return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
					.body(BodyInserters.fromValue(jArray.toString()));
		}catch(Exception e){
			System.out.println("external error: " + e.getMessage());
			return ServerResponse.badRequest().contentType(MediaType.TEXT_PLAIN)
					.body(BodyInserters.fromValue(e.getMessage()));

		}

	}
}
