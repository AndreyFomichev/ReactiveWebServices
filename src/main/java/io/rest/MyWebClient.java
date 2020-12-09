package io.rest;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class MyWebClient implements Runnable{

	String result = "";
	String url; String productId; String categoryId;
	volatile boolean  finished = false;

	public MyWebClient(String url, String productId, String categoryId) {
		this.url = url;
		this.productId = productId;
		this.categoryId = categoryId;
	}

	public String getResult() throws InterruptedException {
		while (!finished)
          Thread.sleep(100);
        return result;
	}

	@Override
	public void run() {
		System.out.println("WebClient started, url=" + url);
		result = org.springframework.web.reactive.function.client.WebClient.create("http://localhost:8080")
    		.get()
			.uri(uriBuilder -> uriBuilder
					.path(url)
						.queryParam("productId", productId)
						.queryParam("categoryId", categoryId)
						.build()
				)
				.accept(MediaType.TEXT_PLAIN)
				.exchange()
				.flatMap(res -> res.bodyToMono(String.class))
				.block();
		finished = true;
	}
}
