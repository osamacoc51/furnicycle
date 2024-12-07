//package com.furnicycle.apigateway.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class CorsConfig {
//	
//	@Bean
//    public WebMvcConfigurer corsConfigurer() {
//        return new WebMvcConfigurer() {
//            @Override
//            public void addCorsMappings(CorsRegistry registry) {
//                registry.addMapping("/**") // Apply to all paths
//                        .allowedOrigins("http://localhost:3000") // Allowed origin
//                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Allowed methods
//                        .allowedHeaders("*") // Allow all headers
//                        .allowCredentials(true); // Allow cookies or credentials
//            }
//        };
//    }
//	
//}