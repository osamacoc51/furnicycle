package com.furnicycle.orderservice.feign;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.furnicycle.orderservice.constants.AppConstants;
import com.furnicycle.orderservice.dto.ProductDTO;
import com.furnicycle.orderservice.exception.ServiceDownException;

@Component
public class ProductServiceFallback implements ProductServiceClient {

	@Override
	public Optional<ProductDTO> getProductById(Integer productId) {
		throw new ServiceDownException(AppConstants.PRODUCT_SERVICE_DOWN);
	}

}
