package com.furnicycle.orderservice.exceptionhandler;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.furnicycle.orderservice.exception.CartNotFoundException;
import com.furnicycle.orderservice.exception.CustomerNotFoundException;
import com.furnicycle.orderservice.exception.OrderNotFoundException;
import com.furnicycle.orderservice.exception.PaymentConfirmationFailedException;
import com.furnicycle.orderservice.exception.PaymentFailedException;
import com.furnicycle.orderservice.exception.ProductNotFoundException;
import com.furnicycle.orderservice.exception.ServiceDownException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@RestControllerAdvice
public class OrderExceptionHandler {
	
	private static final Logger logger = LogManager.getLogger(OrderExceptionHandler.class);
	
	  @ExceptionHandler(ServiceDownException.class)
	    public ResponseEntity<ErrorDetails> handleServiceDownException(ServiceDownException ex, WebRequest request) {
	        // Return the message from the ServiceDownException
		  logger.warn("One of the service is experiencing downtime so order service is unable to communicate"
		  		+ "with other services");
		  ErrorDetails errorDetails=new ErrorDetails(new Date(), ex.getMessage(), request.getDescription(false));
	        return new ResponseEntity<>(errorDetails, HttpStatus.SERVICE_UNAVAILABLE);
	    }
	
	@ExceptionHandler(CartNotFoundException.class)
    public ResponseEntity<ErrorDetails> handleCartNotFoundException(CartNotFoundException ex, WebRequest request) {
		
		logger.warn("CartNotFoundException: " + ex.getMessage(), ex);
        ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }
	
	@ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorDetails> handleProductNotFoundException(ProductNotFoundException ex, WebRequest request) {
		
		logger.warn("ProductNotFoundException: " + ex.getMessage(), ex);
        ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<ErrorDetails> handleCustomerNotFoundException(CustomerNotFoundException ex, WebRequest request) {
    	logger.warn("CustomerNotFoundException: " + ex.getMessage(), ex);
    	ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PaymentFailedException.class)
    public ResponseEntity<ErrorDetails> handlePaymentFailedException(PaymentFailedException ex, WebRequest request) {
    	logger.warn("PaymentFailedException: " + ex.getMessage(), ex);
    	ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.PAYMENT_REQUIRED);
    }

    @ExceptionHandler(PaymentConfirmationFailedException.class)
    public ResponseEntity<ErrorDetails> handlePaymentConfirmationFailedException(PaymentConfirmationFailedException ex, WebRequest request) {
    	logger.warn("PaymentConfirmationFailedException: " + ex.getMessage(), ex);
    	ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<ErrorDetails> handleOrderNotFoundException(OrderNotFoundException ex, WebRequest request) {
    	logger.warn("OrderNotFoundException: " + ex.getMessage(), ex);
    	ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    // Global exception handler
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetails> handleGlobalException(Exception ex, WebRequest request) {
    	logger.warn("Exception: " + ex.getMessage(), ex);
    	ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
