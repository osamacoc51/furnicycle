package com.furnicycle.Ecommerce.exception;

public class CartServiceDownException extends RuntimeException{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2780532863033012204L;

	public CartServiceDownException(String msg) {
		super(msg);
	}

}
