package com.furnicycle.inventoryservice.exceptionhandler;

import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.furnicycle.inventoryservice.exception.InsufficientStockException;
import com.furnicycle.inventoryservice.exception.InventoryNotFoundException;



@RestControllerAdvice
public class GlobalExceptionHandler {
	private static final Logger logger = LogManager.getLogger(GlobalExceptionHandler.class);
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> handleGlobalException(Exception ex, WebRequest request){
		ErrorDetails error=new ErrorDetails(new Date(),ex.getMessage(),request.getDescription(false));
		logger.error("Exception: {}, Request: {}", ex.getMessage(), request.getDescription(false), ex);
		return new ResponseEntity<>(error,HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(InsufficientStockException.class)
	public ResponseEntity<?> handleInsufficientStock(InsufficientStockException ex,
													WebRequest request){
		ErrorDetails error=new ErrorDetails(new Date(),ex.getMessage(),request.getDescription(false));
		logger.error("InsufficientStockException: {}, Request: {}", ex.getMessage(), request.getDescription(false), ex);
		return new ResponseEntity<>(error,HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler(InventoryNotFoundException.class)
	public ResponseEntity<?> handleInventoryNotFound(InventoryNotFoundException ex,
												WebRequest request){
		ErrorDetails errorDetails=new ErrorDetails(new Date(),ex.getMessage(),request.getDescription(false));
		logger.error("InventoryNotFoundException: {}, Request: {}", ex.getMessage(), request.getDescription(false), ex);
		return new ResponseEntity<>(errorDetails,HttpStatus.NOT_FOUND);
	}
}
