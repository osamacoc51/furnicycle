package com.furnicycle.Ecommerce.exception;

public class ProductNotFoundException extends RuntimeException{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5115566161795915463L;

	public ProductNotFoundException(String msg) {
		super(msg);
	}
}
