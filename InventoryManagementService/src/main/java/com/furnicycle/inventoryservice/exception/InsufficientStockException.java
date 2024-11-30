package com.furnicycle.inventoryservice.exception;

public class InsufficientStockException extends RuntimeException{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7787351832052702031L;

	public InsufficientStockException(String msg) {
		super(msg);
	}

}
