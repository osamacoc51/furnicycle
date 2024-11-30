package com.furnicycle.orderservice.feign;

import org.springframework.stereotype.Component;

import com.furnicycle.orderservice.constants.AppConstants;
import com.furnicycle.orderservice.dto.InventoryDTO;
import com.furnicycle.orderservice.exception.ServiceDownException;



@Component
public class InventoryServiceFallback implements InventoryServiceClient{

	@Override
	public InventoryDTO increaseAvailableStockAfterOrderCancel(Integer productId, Integer quantity) {
		throw new ServiceDownException(AppConstants.INVENTORY_SERVICE_DOWN);
	}

	@Override
	public String reserveStockForOrder(Integer productId, Integer quantity) {
		// TODO Auto-generated method stub
		throw new ServiceDownException(AppConstants.INVENTORY_SERVICE_DOWN);
		
	}

	@Override
	public String releaseReservedStock(Integer productId, Integer quantity) {
		// TODO Auto-generated method stub
		throw new ServiceDownException(AppConstants.INVENTORY_SERVICE_DOWN);
		
	}

	@Override
	public InventoryDTO reduceReservedStockAfterOrder(Integer productId, Integer quantity) {
		// TODO Auto-generated method stub
		throw new ServiceDownException(AppConstants.INVENTORY_SERVICE_DOWN);
		
	}

	@Override
	public InventoryDTO addStock(Integer productId, Integer quantity) {
		// TODO Auto-generated method stub
		throw new ServiceDownException(AppConstants.INVENTORY_SERVICE_DOWN);
		
	}

}
