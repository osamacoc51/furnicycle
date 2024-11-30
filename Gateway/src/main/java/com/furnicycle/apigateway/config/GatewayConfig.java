package com.furnicycle.apigateway.config;

//import org.springframework.cloud.gateway.route.RouteLocator;
//import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class GatewayConfig {
//
//    @Bean
//    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
//        return builder.routes()
//            .route("user-service", r -> r.path("/customer/**")
//                .filters(f -> f.circuitBreaker(c -> c.setName("userServiceCircuitBreaker")
//                         .setFallbackUri("forward:/fallback/user-service")))
//                .uri("lb://user-service"))
//            
//            .route("product-service", r -> r.path("/product/**")
//                .filters(f -> f.circuitBreaker(c -> c.setName("productServiceCircuitBreaker")
//                         .setFallbackUri("forward:/fallback/product-service")))
//                .uri("lb://product-service"))
//            
//            .route("cart-service", r -> r.path("/cart/**")
//                .filters(f -> f.circuitBreaker(c -> c.setName("cartServiceCircuitBreaker")
//                         .setFallbackUri("forward:/fallback/cart-service")))
//                .uri("lb://cart-service"))
//            
//            .build();
//    }
//}
