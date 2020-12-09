package io.rest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class ExternalRouter {

    @Bean
    public RouterFunction<ServerResponse> externalRoute(ExternalHandler externalHandler) {
        System.out.println("externalRoute started");
        return RouterFunctions
                .route(RequestPredicates.GET("/external")
                                .and(RequestPredicates.accept(MediaType.TEXT_PLAIN))
                        , externalHandler::external);
    }

    @Bean
    public RouterFunction<ServerResponse> discountRoute(DiscountHandler discountHandler) {
        System.out.println("discountRoute started");
        return RouterFunctions
                .route(RequestPredicates.GET("/discount")
                                .and(RequestPredicates.accept(MediaType.TEXT_PLAIN))
                        , discountHandler::discount);
    }

    @Bean
    public RouterFunction<ServerResponse> warehouseRoute(WareHouseHandler wareHouseHandler) {
        System.out.println("warehouseRoute started");
        return RouterFunctions
                .route(RequestPredicates.GET("/warehouse")
                                .and(RequestPredicates.accept(MediaType.TEXT_PLAIN))
                        , wareHouseHandler::warehouse);
    }

}
