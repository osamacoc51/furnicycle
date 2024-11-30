package com.furnicycle.orderservice.exception;

public class OrderNotFoundException extends RuntimeException{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6253253637899868294L;

	public OrderNotFoundException(String msg) {
		super(msg);
	}

}
