package com.furnicycle.Ecommerce.exception;

public class InventoryServiceDownException extends RuntimeException{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7275052040814014876L;

	public InventoryServiceDownException(String msg) {
		super(msg);
	}
}
