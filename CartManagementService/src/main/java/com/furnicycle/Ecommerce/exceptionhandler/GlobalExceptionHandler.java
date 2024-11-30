package com.furnicycle.Ecommerce.exceptionhandler;

import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.furnicycle.Ecommerce.exception.CartNotFoundException;
import com.furnicycle.Ecommerce.exception.InsufficientStockException;
import com.furnicycle.Ecommerce.exception.InvalidProductException;
import com.furnicycle.Ecommerce.exception.InventoryServiceDownException;
import com.furnicycle.Ecommerce.exception.ProductServiceDownException;


@RestControllerAdvice
public class GlobalExceptionHandler {
	
	
	private static final Logger logger = LogManager.getLogger(GlobalExceptionHandler.class);
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> handleGlobalException(Exception ex, WebRequest request){
		logger.error("Global Exception: {} at {}", ex.getMessage(), request.getDescription(false));
		ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
		
	}
	
	
	
	@ExceptionHandler(ProductServiceDownException.class)
	public ResponseEntity<?> handleProductServiceDownException(ProductServiceDownException ex, WebRequest request){
		logger.error("Product Service Down Exception: {} at {}", ex.getMessage(), request.getDescription(false));
		ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.SERVICE_UNAVAILABLE);
	}
	
	@ExceptionHandler(InvalidProductException.class)
	public ResponseEntity<?> handleInvalidProductException(InvalidProductException ex, WebRequest request){
		logger.error("Invalid Product Exception: {} at {}", ex.getMessage(), request.getDescription(false));
		ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
		
	}
	@ExceptionHandler(CartNotFoundException.class)
	public ResponseEntity<?> handleCartNotFoundException(CartNotFoundException ex, WebRequest request){
		logger.error("Cart Not Found Exception: {} at {}", ex.getMessage(), request.getDescription(false));
		ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
		
	}
	
	@ExceptionHandler(InventoryServiceDownException.class)
	public ResponseEntity<?> handleInventoryServiceDown(InventoryServiceDownException ex, WebRequest request){
		logger.error("Inventory Service Down Exception: {} at {}", ex.getMessage(), request.getDescription(false));
		ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.SERVICE_UNAVAILABLE);
		
	}
	@ExceptionHandler(InsufficientStockException.class)
	public ResponseEntity<?> handleInsufficientStock(InsufficientStockException ex, WebRequest request){
		logger.error("Insufficient Exception: {} at {}", ex.getMessage(), request.getDescription(false));
		ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.CONFLICT);
		
	}
}
