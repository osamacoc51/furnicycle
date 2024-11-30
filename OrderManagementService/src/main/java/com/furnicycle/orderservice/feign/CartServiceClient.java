package com.furnicycle.orderservice.feign;

import java.util.Optional;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.furnicycle.orderservice.dto.CartDTO;

@FeignClient(name = "cart-service",fallback=CartServiceFallback.class)
public interface CartServiceClient {

    @GetMapping("/cart/{cartId}")
    Optional<CartDTO> getCartById(@PathVariable("cartId") Integer cartId);
    
    @DeleteMapping("/cart/{cartId}/clear")
    String clearCart(@PathVariable("cartId") Integer cartId);
}
