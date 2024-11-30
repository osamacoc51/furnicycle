package com.furnicycle.orderservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(name = "user-service", fallback = CustomerServiceFallback.class)
public interface CustomerServiceClient {
	
	@GetMapping("/customer/validate/{customerId}")
    boolean isValidCustomer(@PathVariable("customerId") Integer customerId);
}
