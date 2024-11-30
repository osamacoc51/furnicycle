package com.furnicycle.orderservice.service;

import java.util.List;

import com.furnicycle.orderservice.dto.OrderDTO;

public interface OrderService {
	OrderDTO createOrder(Integer customerId, Integer cartId);
    String cancelOrder(Integer orderId);
    List<OrderDTO> viewOrdersByCustomer(Integer customerId);
    OrderDTO viewOrderById(Integer orderId);
    OrderDTO confirmPayment(Integer orderId, String paymentId);
}
