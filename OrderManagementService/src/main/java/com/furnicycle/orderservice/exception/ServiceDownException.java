package com.furnicycle.orderservice.exception;

public class ServiceDownException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4297218332239787918L;

	public ServiceDownException(String msg) {
		super(msg);
	}

}
