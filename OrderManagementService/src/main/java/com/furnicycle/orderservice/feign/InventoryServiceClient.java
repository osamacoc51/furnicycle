package com.furnicycle.orderservice.feign;

import org.springframework.cloud.openfeign.FeignClient;

//import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.furnicycle.orderservice.dto.InventoryDTO;




@FeignClient(name = "inventory-service",fallback=InventoryServiceFallback.class)
public interface InventoryServiceClient {
//	@GetMapping("/inventory/{productId}")
//    InventoryDTO getInventoryByProductId(@PathVariable("productId") Integer productId);
	
	@PutMapping("/inventory/increaseAvailableStock")
    InventoryDTO increaseAvailableStockAfterOrderCancel(
            @RequestParam("productId") Integer productId,
            @RequestParam("quantity") Integer quantity);

    @PostMapping("/inventory/{productId}/reserve")
    String reserveStockForOrder(@PathVariable("productId") Integer productId, 
                              @RequestParam("quantity") Integer quantity);

    @PostMapping("/inventory/{productId}/release")
    String releaseReservedStock(@PathVariable("productId") Integer productId, 
                              @RequestParam("quantity") Integer quantity);

    @PostMapping("/inventory/{productId}/reduce")
    InventoryDTO reduceReservedStockAfterOrder(@PathVariable("productId") Integer productId, 
                                       @RequestParam("quantity") Integer quantity);

    @PostMapping("/inventory/{productId}/add")
    InventoryDTO addStock(@PathVariable("productId") Integer productId, 
                  @RequestParam("quantity") Integer quantity);
}
