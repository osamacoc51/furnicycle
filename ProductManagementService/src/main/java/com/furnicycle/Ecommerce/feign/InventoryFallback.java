package com.furnicycle.Ecommerce.feign;

import org.springframework.stereotype.Component;

import com.furnicycle.Ecommerce.constants.AppConstants;
import com.furnicycle.Ecommerce.exception.InventoryServiceDownException;

@Component
public class InventoryFallback implements InventoryClient{

	@Override
	public void reserveStockForOrder(Integer productId, Integer quantity) {
		throw new InventoryServiceDownException(AppConstants.INVENTORY_SERVICE_DOWN);
		
	}

	@Override
	public void releaseReservedStock(Integer productId, Integer quantity) {
		// TODO Auto-generated method stub
		throw new InventoryServiceDownException(AppConstants.INVENTORY_SERVICE_DOWN);
	}

	@Override
	public void reduceReservedStockAfterOrder(Integer productId, Integer quantity) {
		// TODO Auto-generated method stub
		throw new InventoryServiceDownException(AppConstants.INVENTORY_SERVICE_DOWN);
	}

	@Override
	public void addStock(Integer productId, Integer quantity) {
		// TODO Auto-generated method stub
		throw new InventoryServiceDownException(AppConstants.INVENTORY_SERVICE_DOWN);
		
	}

}
