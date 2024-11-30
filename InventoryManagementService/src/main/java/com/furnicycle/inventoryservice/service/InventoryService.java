package com.furnicycle.inventoryservice.service;

import com.furnicycle.inventoryservice.dto.InventoryDTO;

public interface InventoryService {
	InventoryDTO addStock(Integer productId, Integer quantity);

    InventoryDTO removeStock(Integer productId,  Integer quantity);

    boolean reserveStockForOrder(Integer productId, Integer quantity);

    boolean releaseReservedStock(Integer productId, Integer quantity);

    InventoryDTO reduceReservedStockAfterOrder(Integer productId, Integer quantity);

    InventoryDTO getInventoryByProductId(Integer productId);
    
    InventoryDTO increaseAvailableStockAfterOrderCancel(Integer productId, Integer quantity);
}
