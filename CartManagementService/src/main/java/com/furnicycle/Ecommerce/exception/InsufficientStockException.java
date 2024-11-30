package com.furnicycle.Ecommerce.exception;

public class InsufficientStockException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -309703416095356433L;

	public InsufficientStockException(String msg) {
		super(msg);
	}
}
