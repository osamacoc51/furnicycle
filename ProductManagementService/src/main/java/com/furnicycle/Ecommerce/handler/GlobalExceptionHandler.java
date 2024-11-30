package com.furnicycle.Ecommerce.handler;

import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.furnicycle.Ecommerce.exception.CategoryAlreadyExistsException;
import com.furnicycle.Ecommerce.exception.CategoryNotFoundException;
import com.furnicycle.Ecommerce.exception.InventoryServiceDownException;
import com.furnicycle.Ecommerce.exception.ProductAlreadyExistsException;
import com.furnicycle.Ecommerce.exception.ProductNotFoundException;



@RestControllerAdvice
public class GlobalExceptionHandler {
	
	private static final Logger logger = LogManager.getLogger(GlobalExceptionHandler.class);
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> handleGlobalException(Exception ex, WebRequest request){
		ErrorDetails error=new ErrorDetails(new Date(),ex.getMessage(),request.getDescription(false));
		logger.error("Exception: {}, Request: {}", ex.getMessage(), request.getDescription(false), ex);
		return new ResponseEntity<>(error,HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(CategoryAlreadyExistsException.class)
	public ResponseEntity<?> handleCategoryExists(CategoryAlreadyExistsException ex,
													WebRequest request){
		ErrorDetails error=new ErrorDetails(new Date(),ex.getMessage(),request.getDescription(false));
		logger.error("CategoryAlreadyExistsException: {}, Request: {}", ex.getMessage(), request.getDescription(false), ex);
		return new ResponseEntity<>(error,HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler(CategoryNotFoundException.class)
	public ResponseEntity<?> handleCategoryNotFound(CategoryNotFoundException ex,
												WebRequest request){
		ErrorDetails errorDetails=new ErrorDetails(new Date(),ex.getMessage(),request.getDescription(false));
		logger.error("CategoryNotFoundException: {}, Request: {}", ex.getMessage(), request.getDescription(false), ex);
		return new ResponseEntity<>(errorDetails,HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(ProductAlreadyExistsException.class)
	public ResponseEntity<?> handleProductAlreadyExists(ProductAlreadyExistsException ex, 
														WebRequest request){
		ErrorDetails errorDetails=new ErrorDetails(new Date(),ex.getMessage(),request.getDescription(false));
		logger.error("ProductAlreadyExistsException: {}, Request: {}", ex.getMessage(), request.getDescription(false), ex);
		return new ResponseEntity<>(errorDetails,HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler(ProductNotFoundException.class)
	public ResponseEntity<?> handleProductNotFound(ProductNotFoundException ex,
												WebRequest request){
		ErrorDetails errorDetails=new ErrorDetails(new Date(),ex.getMessage(),request.getDescription(false));
		logger.error("ProductNotFoundException: {}, Request: {}", ex.getMessage(), request.getDescription(false), ex);
		return new ResponseEntity<>(errorDetails,HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(InventoryServiceDownException.class)
	public ResponseEntity<?> handleInventoryServiceDown(InventoryServiceDownException ex, 
														WebRequest request){
		ErrorDetails errorDetails=new ErrorDetails(new Date(),ex.getMessage(),request.getDescription(false));
		logger.error("InventoryServiceDownException: {}, Request: {}", ex.getMessage(), request.getDescription(false), ex);
		return new ResponseEntity<>(errorDetails,HttpStatus.SERVICE_UNAVAILABLE);
	}
}
