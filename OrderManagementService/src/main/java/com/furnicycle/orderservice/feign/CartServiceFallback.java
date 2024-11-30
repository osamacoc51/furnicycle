package com.furnicycle.orderservice.feign;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.furnicycle.orderservice.constants.AppConstants;
import com.furnicycle.orderservice.dto.CartDTO;
import com.furnicycle.orderservice.exception.ServiceDownException;

@Component
public class CartServiceFallback implements CartServiceClient{

	@Override
	public Optional<CartDTO> getCartById(Integer cartId) {
		throw new ServiceDownException(AppConstants.CART_SERVICE_DOWN);
	}

	@Override
	public String clearCart(Integer cartId) {
		throw new ServiceDownException(AppConstants.CART_SERVICE_DOWN);
	}

}
