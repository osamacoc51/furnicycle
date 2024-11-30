package com.furnicycle.Ecommerce.exception;

public class InvalidProductException extends RuntimeException{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8175062664027181946L;

	public InvalidProductException(String msg) {
		super(msg);
	}

}
