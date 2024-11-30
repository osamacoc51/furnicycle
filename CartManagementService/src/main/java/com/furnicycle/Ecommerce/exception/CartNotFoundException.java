package com.furnicycle.Ecommerce.exception;

public class CartNotFoundException extends RuntimeException{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4892947826491986776L;

	public CartNotFoundException(String msg) {
		super(msg);
	}

}
