package com.furnicycle.orderservice.feign;

import java.util.Optional;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.furnicycle.orderservice.dto.ProductDTO;

@FeignClient(name = "product-service",fallback=ProductServiceFallback.class)
public interface ProductServiceClient {

    @GetMapping("/product/id/{productId}")
    Optional<ProductDTO> getProductById(@PathVariable("productId") Integer productId);
}