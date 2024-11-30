package com.furnicycle.Ecommerce.exception;

public class CategoryAlreadyExistsException extends RuntimeException{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 176485324350010146L;

	public CategoryAlreadyExistsException(String msg) {
		super(msg);
	}

}
