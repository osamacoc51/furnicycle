package com.furnicycle.inventoryservice.exception;

public class InventoryNotFoundException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2033559634060754667L;

	public InventoryNotFoundException(String msg) {
		super(msg);
	}
}
