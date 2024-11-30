package com.furnicycle.Ecommerce.exception;

public class ProductAlreadyExistsException extends RuntimeException{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3192774783563265231L;

	public ProductAlreadyExistsException(String msg) {
		super(msg);
	}

}
