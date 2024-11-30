package com.furnicycle.Ecommerce.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.furnicycle.Ecommerce.dto.InventoryDTO;

@FeignClient(name = "inventory-service",fallback=InventoryServiceFallback.class)
public interface InventoryServiceClient {
	@GetMapping("/inventory/{productId}")
    InventoryDTO getInventoryByProductId(@PathVariable("productId") Integer productId);

    @PostMapping("/inventory/{productId}/reserve")
    void reserveStockForOrder(@PathVariable("productId") Integer productId, 
                              @RequestParam("quantity") Integer quantity);

    @PostMapping("/inventory/{productId}/release")
    void releaseReservedStock(@PathVariable("productId") Integer productId, 
                              @RequestParam("quantity") Integer quantity);

    @PostMapping("/inventory/{productId}/reduce")
    void reduceReservedStockAfterOrder(@PathVariable("productId") Integer productId, 
                                       @RequestParam("quantity") Integer quantity);

    @PostMapping("/inventory/{productId}/add")
    void addStock(@PathVariable("productId") Integer productId, 
                  @RequestParam("quantity") Integer quantity);
}
