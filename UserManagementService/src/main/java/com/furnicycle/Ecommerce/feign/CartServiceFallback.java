package com.furnicycle.Ecommerce.feign;

import org.springframework.stereotype.Component;

import com.furnicycle.Ecommerce.exception.CartServiceDownException;

@Component
public class CartServiceFallback implements CartServiceClient{

	@Override
	public String createCart(Integer customerId) {
		throw new CartServiceDownException("Cart Service is facing downtime \n"+
				"It will be fixed soon \n Comeback after few time");
	}

}
