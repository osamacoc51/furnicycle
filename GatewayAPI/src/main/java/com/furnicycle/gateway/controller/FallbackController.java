package com.furnicycle.gateway.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.furnicycle.gateway.constants.AppConstants;

@RestController
@RequestMapping("/fallback")
public class FallbackController {
 
	@GetMapping("/user")
	public ResponseEntity<String> userServiceFallback() {
		return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
				.body(AppConstants.USER_SERVICE_DOWN);
	}
 
	@RequestMapping("/product")
	public ResponseEntity<String> productServiceFallback() {
		return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
				.body(AppConstants.PRODUCT_SERVICE_DOWN);
	}
 
	@GetMapping("/cart")
	public ResponseEntity<String> cartServiceFallback() {
		return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
				.body(AppConstants.CART_SERVICE_DOWN);
	}
	
	@GetMapping("/orders")
	public ResponseEntity<String> orderServiceHandler(){
		return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
				.body(AppConstants.ORDER_SERVICE_DOWN);
	}
	
	@RequestMapping("/inventory")
	public ResponseEntity<String> inventoryServiceHandler(){
		return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
				.body(AppConstants.INVENTORY_SERVICE_DOWN);
	}
}
