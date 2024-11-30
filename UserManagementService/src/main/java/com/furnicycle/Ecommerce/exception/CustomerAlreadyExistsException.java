package com.furnicycle.Ecommerce.exception;

public class CustomerAlreadyExistsException extends RuntimeException{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 9222996077533912701L;

	public CustomerAlreadyExistsException(String msg) {
		super(msg);
	}

}
