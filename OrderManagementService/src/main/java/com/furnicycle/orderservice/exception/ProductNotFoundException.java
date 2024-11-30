package com.furnicycle.orderservice.exception;

public class ProductNotFoundException extends RuntimeException{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1204484653948293478L;

	public ProductNotFoundException(String msg) {
		super(msg);
	}

}
