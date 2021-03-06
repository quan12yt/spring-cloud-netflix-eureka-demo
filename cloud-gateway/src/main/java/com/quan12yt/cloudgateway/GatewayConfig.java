package com.quan12yt.cloudgateway;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator gateRouteLocator(RouteLocatorBuilder builder){
        return builder.routes()
                .route(r -> r.path("/**")
                        .uri("lb://CLIENT-SERVICE"))

                .route(r -> r.path("/**")
                        .uri("lb://LOAD-SERVICE"))
                .build();
    }
}
