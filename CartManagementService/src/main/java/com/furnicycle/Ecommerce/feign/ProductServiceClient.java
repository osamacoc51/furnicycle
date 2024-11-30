package com.furnicycle.Ecommerce.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;




@FeignClient(name = "product-service")
public interface ProductServiceClient {
    
    @GetMapping("/product/validate/{productId}")
    boolean isValidProduct(@PathVariable("productId") Integer productId);
}