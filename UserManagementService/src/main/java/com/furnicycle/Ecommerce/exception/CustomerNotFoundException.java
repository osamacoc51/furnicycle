package com.furnicycle.Ecommerce.exception;

public class CustomerNotFoundException extends RuntimeException{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5144693456689459707L;

	public CustomerNotFoundException(String msg) {
		super(msg);
		
	}

}
