package com.furnicycle.orderservice.service;


import java.util.Optional;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.furnicycle.orderservice.constants.AppConstants;
import com.furnicycle.orderservice.dto.OrderDTO;
import com.furnicycle.orderservice.dto.PaymentResponseDTO;
import com.furnicycle.orderservice.entity.OrderEntity;
import com.furnicycle.orderservice.exception.PaymentConfirmationFailedException;
import com.furnicycle.orderservice.repository.OrderRepository;

@Service
public class DummyPaymentServiceImpl implements PaymentService{
	
	@Autowired
	private OrderRepository orderRepository;
	
	private static final Logger logger = LogManager.getLogger(DummyPaymentServiceImpl.class);
	
	@Override
    public PaymentResponseDTO createPayment(OrderDTO orderDTO) {
		logger.info("Initiating payment creation for order: {}", orderDTO.getOrderId());
      
        String paymentId = UUID.randomUUID().toString();
        
        logger.info("Payment created with ID: {} for Order ID: {}", paymentId, orderDTO.getOrderId());
        return new PaymentResponseDTO(paymentId, orderDTO.getTotalAmount(), "INR", AppConstants.PAYMENT_STATUS_CREATED);
    }

    @Override
    public PaymentResponseDTO confirmPayment(Integer orderId,String paymentId) {
    	logger.info("Confirming payment for Order ID: {} with Payment ID: {}", orderId, paymentId);
    	Optional<OrderEntity> op=orderRepository.findById(orderId);
    	if(op.isPresent()) {
    		OrderEntity order = op.get();
    		PaymentResponseDTO paymentResponse = new PaymentResponseDTO(paymentId, order.getTotalAmount(), "INR", AppConstants.PAYMENT_STATUS_PAID); // Assume successful payment
            
    		logger.info("Payment confirmed for Order ID: {} with Payment ID: {}", orderId, paymentId);
            
    		return paymentResponse;
    	}
    	else {
    		throw new PaymentConfirmationFailedException(AppConstants.PAYMENT_CONFIRMATION_FAILED+orderId);
    	}
    }
}
