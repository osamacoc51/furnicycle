package com.furnicycle.Ecommerce.exception;

public class CategoryNotFoundException extends RuntimeException{
	/**
	 * 
	 */
	private static final long serialVersionUID = -25515870482145140L;

	public CategoryNotFoundException(String msg) {
		super(msg);
	}
}
