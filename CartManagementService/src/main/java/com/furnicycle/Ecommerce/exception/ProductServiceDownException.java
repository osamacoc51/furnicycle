package com.furnicycle.Ecommerce.exception;

public class ProductServiceDownException extends RuntimeException{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8165883741900918050L;

	public ProductServiceDownException(String msg) {
		super(msg);
	}
}
