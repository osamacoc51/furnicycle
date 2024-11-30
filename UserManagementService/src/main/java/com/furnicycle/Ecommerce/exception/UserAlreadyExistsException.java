package com.furnicycle.Ecommerce.exception;

public class UserAlreadyExistsException extends RuntimeException{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3508222086335788089L;

	public UserAlreadyExistsException(String msg) {
		super(msg);
	}

}
