package com.furnicycle.orderservice.exception;

public class CartNotFoundException extends RuntimeException{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8030368256483646048L;

	public CartNotFoundException(String msg) {
		super(msg);
	}

}
