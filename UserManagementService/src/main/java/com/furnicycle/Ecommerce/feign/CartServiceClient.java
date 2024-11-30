package com.furnicycle.Ecommerce.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;



@FeignClient(name = "cart-service",fallback=CartServiceFallback.class)
public interface CartServiceClient {

    @PostMapping("/cart/create/{customerId}")
    String createCart(@PathVariable("customerId") Integer customerId);
}
