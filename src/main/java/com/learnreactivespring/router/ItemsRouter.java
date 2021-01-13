package com.learnreactivespring.router;

import com.learnreactivespring.handler.ItemsHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class ItemsRouter {

    @Bean
    public RouterFunction<ServerResponse> indexRoute(ItemsHandler itemsHandler) {
        return RouterFunctions
                .route(GET("/v2/items")
                        .and(accept(MediaType.APPLICATION_JSON)), itemsHandler::index);
    }

    @Bean
    public RouterFunction<ServerResponse> getRoute(ItemsHandler itemsHandler) {
        return RouterFunctions
                .route(GET("/v2/items/{id}")
                        .and(accept(MediaType.APPLICATION_JSON)), itemsHandler::get);
    }

    @Bean
    public RouterFunction<ServerResponse> createRoute(ItemsHandler itemsHandler) {
        return RouterFunctions
                .route(POST("/v2/items")
                        .and(accept(MediaType.APPLICATION_JSON)), itemsHandler::create);
    }

    @Bean
    public RouterFunction<ServerResponse> deleteRoute(ItemsHandler itemsHandler) {
        return RouterFunctions
                .route(DELETE("/v2/items/{id}")
                        .and(accept(MediaType.APPLICATION_JSON)), itemsHandler::delete);
    }

    @Bean
    public RouterFunction<ServerResponse> updateRoute(ItemsHandler itemsHandler) {
        return RouterFunctions
                .route(PUT("/v2/items")
                        .and(accept(MediaType.APPLICATION_JSON)), itemsHandler::update);
    }
}
