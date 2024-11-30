package com.furnicycle.orderservice.service;

import com.furnicycle.orderservice.dto.OrderDTO;
import com.furnicycle.orderservice.dto.PaymentResponseDTO;

public interface PaymentService {
	PaymentResponseDTO createPayment(OrderDTO orderDTO);
    PaymentResponseDTO confirmPayment(Integer orderId,String paymentId);
}
