package com.furnicycle.orderservice.exception;

public class PaymentConfirmationFailedException extends RuntimeException{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2007831862674675901L;

	public PaymentConfirmationFailedException(String msg) {
		super(msg);
	}

}
