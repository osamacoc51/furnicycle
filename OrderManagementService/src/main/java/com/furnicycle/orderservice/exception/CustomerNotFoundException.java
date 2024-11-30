package com.furnicycle.orderservice.exception;

public class CustomerNotFoundException extends RuntimeException{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8384108433893933689L;

	public CustomerNotFoundException(String msg) {
		super(msg);
	}

}
