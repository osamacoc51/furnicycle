package com.furnicycle.orderservice.exception;

public class PaymentFailedException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6413334529449050583L;

	public PaymentFailedException(String msg) {
		super(msg);
	}

}
