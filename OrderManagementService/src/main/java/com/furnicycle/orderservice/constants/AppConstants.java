package com.furnicycle.orderservice.constants;

public class AppConstants {
	
	public static final String CUSTOMER_NOT_FOUND="Can't create order as No Customer Found with Id : ";
	
	public static final String CART_NOT_FOUND="Can't create order as No Cart Found with Id : ";
	
	public static final String PRODUCT_NOT_FOUND="Can't create order as No Product Found with Id : ";
	
	public static final String PAYMENT_FAILED="Payment failed. Order could not be placed";
	
	public static final String PAYMENT_STATUS_CREATED=" PAYMENT CREATED";
	public static final String PAYMENT_STATUS_PAID="PAID"; 
	
	public static final String ORDER_ALREADY_CANCELLED="Order Already Cancelled with OrderId: ";
	
	public static final String ORDER_STATUS_CANCELED="ORDER CANCELLED";
	public static final String ORDER_STATUS_PENDING="ORDER PENDING";
	public static final String ORDER_STATUS_CONFIREMED="ORDER CONFIRMED";
	
	public static final String ORDER_NOT_FOUND="No Order found with Id:"; 
	
	public static final String CANCEL_ORDER="Order Cancelled Successfully";
	
	public static final String PAYMENT_CONFIRMATION_FAILED="Payment confirmation failed for paymentId: ";
	
	public static final String CUSTOMER_SERVICE_DOWN="User service is currently facing downtime \n"+
	"It will be fixed soon";
	
	public static final String CART_SERVICE_DOWN="Cart service is currently facing downtime \n"+
			"It will be fixed soon";
	
	public static final String PRODUCT_SERVICE_DOWN="Productservice is currently facing downtime \n"+
			"It will be fixed soon";
			
	public static final String INVENTORY_SERVICE_DOWN="INVENTORY service is currently facing downtime \n"+
			"It will be fixed soon";
}
