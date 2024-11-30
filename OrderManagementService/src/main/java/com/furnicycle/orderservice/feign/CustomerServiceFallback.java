package com.furnicycle.orderservice.feign;

import org.springframework.stereotype.Component;

import com.furnicycle.orderservice.constants.AppConstants;
import com.furnicycle.orderservice.exception.ServiceDownException;

@Component
public class CustomerServiceFallback implements CustomerServiceClient {
    @Override
    public boolean isValidCustomer(Integer id) {
        throw new ServiceDownException(AppConstants.CUSTOMER_SERVICE_DOWN);
    }
}
