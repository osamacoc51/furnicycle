package com.furnicycle.orderservice.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.furnicycle.orderservice.constants.AppConstants;
import com.furnicycle.orderservice.dto.CartDTO;
import com.furnicycle.orderservice.dto.CartItemDTO;
import com.furnicycle.orderservice.dto.OrderDTO;
import com.furnicycle.orderservice.dto.PaymentResponseDTO;
import com.furnicycle.orderservice.dto.ProductDTO;
import com.furnicycle.orderservice.entity.OrderEntity;
import com.furnicycle.orderservice.exception.CartNotFoundException;
import com.furnicycle.orderservice.exception.CustomerNotFoundException;
import com.furnicycle.orderservice.exception.OrderNotFoundException;
import com.furnicycle.orderservice.exception.PaymentConfirmationFailedException;
import com.furnicycle.orderservice.exception.PaymentFailedException;
import com.furnicycle.orderservice.exception.ProductNotFoundException;
import com.furnicycle.orderservice.feign.CartServiceClient;
import com.furnicycle.orderservice.feign.CustomerServiceClient;
import com.furnicycle.orderservice.feign.InventoryServiceClient;
import com.furnicycle.orderservice.feign.ProductServiceClient;
import com.furnicycle.orderservice.mapper.OrderMapper;
import com.furnicycle.orderservice.repository.OrderRepository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jakarta.transaction.Transactional;

@Service
public class OrderServiceImpl implements OrderService {
	
	@Autowired
	private PaymentService paymentService;
	
	@Autowired
	private CartServiceClient cartServiceClient;
	
	@Autowired
	private ProductServiceClient productServiceClient;
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private OrderMapper orderMapper;
	
	@Autowired
	private CustomerServiceClient customerServiceClient;
	
	@Autowired
	private InventoryServiceClient inventoryServiceClient;
	
	private static final Logger logger = LogManager.getLogger(OrderServiceImpl.class);
	

	@Override
	@Transactional
	public OrderDTO createOrder(Integer customerId, Integer cartId) {
		logger.info("Creating order for customer ID: {} and cart ID: {}", customerId, cartId);
		if (!customerServiceClient.isValidCustomer(customerId)) {
			logger.warn("Customer not found with ID: {}", customerId);
			throw new CustomerNotFoundException(AppConstants.CUSTOMER_NOT_FOUND+customerId);
		}
		
		Optional<CartDTO> cartDTO = cartServiceClient.getCartById(cartId);
		if (cartDTO.isEmpty()) {
			logger.warn("Cart not found with ID: {}", cartId);
			throw new CartNotFoundException(AppConstants.CART_NOT_FOUND+cartId);
		}
		Double totalAmount=cartDTO.get().getCartItemDtos().stream()
								.mapToDouble(item->{
									Optional<ProductDTO> product=productServiceClient.getProductById(item.getProductId());
									if(product.isEmpty()) {
										throw new ProductNotFoundException(AppConstants.PRODUCT_NOT_FOUND+item.getProductId());
									}
									else {
										return item.getQuantity()*product.get().getProductPrice();
									}
								})
								.sum();
		
		OrderEntity orderEntity = new OrderEntity();
        orderEntity.setCustomerId(customerId);
        orderEntity.setOrderDate(new Date());
        orderEntity.setOrderStatus(AppConstants.ORDER_STATUS_PENDING);
        orderEntity.setTotalAmount(totalAmount);
        orderEntity.setCartId(cartId);
        
        logger.info("Saving Order  with ID: {}", orderEntity.getOrderId());
        
        orderEntity = orderRepository.save(orderEntity);
        
        logger.info("Order created with ID: {}", orderEntity.getOrderId());
        // Create payment
        OrderDTO orderDTO = orderMapper.toOrderDTO(orderEntity);
        PaymentResponseDTO paymentResponse = paymentService.createPayment(orderDTO);

        if (AppConstants.PAYMENT_STATUS_CREATED.equals(paymentResponse.getStatus())) {
        	logger.info("Payment created successfully for Order ID: {}", orderEntity.getOrderId());
        	
            CartDTO cart = cartServiceClient.getCartById(cartId).get();
            logger.info("Clearingstock from inventory as order is being done");
            for (CartItemDTO item : cart.getCartItemDtos()) {
            	logger.info("Removing stock of product "+item.getProductId()+" from inventory");
                inventoryServiceClient.reduceReservedStockAfterOrder(item.getProductId(),item.getQuantity());
            }
//            logger.info("As order is being done so clearing cart");
//            cartServiceClient.clearCart(cartId);
            logger.info("Order placed for customer with Id"+customerId);
            return orderDTO;
        } else {
        	logger.error("Payment failed for Order ID: {}", orderEntity.getOrderId());
            throw new PaymentFailedException(AppConstants.PAYMENT_FAILED);
        }
	}
	

	
	
	@Override
	@Transactional
	public String cancelOrder(Integer orderId) {
		
		logger.info("Attempting to cancel order with ID: {}", orderId);
		Optional<OrderEntity> orderOptional = orderRepository.findById(orderId);
        if (orderOptional.isPresent()) {
            OrderEntity orderEntity = orderOptional.get();
            if (!AppConstants.ORDER_STATUS_CANCELED.equals(orderEntity.getOrderStatus())) {
            	logger.info("calling cart service to get cartitems based on which stock is adjusted on inventory");
            	Optional<CartDTO> cart = cartServiceClient.getCartById(orderEntity.getCartId());
                for (CartItemDTO item : cart.get().getCartItemDtos()) {
                	logger.info("Releasing stock so the the stock will be available in inventory");
                    inventoryServiceClient.increaseAvailableStockAfterOrderCancel(item.getProductId(),  item.getQuantity());
                }
                
                
            	logger.info("Cancelling order with Order ID: {} ", orderId);
                orderEntity.setOrderStatus(AppConstants.ORDER_STATUS_CANCELED);
                orderRepository.save(orderEntity);
                logger.info("Order ID: {} canceled successfully.", orderId);
                return AppConstants.CANCEL_ORDER;
            }
            else {
            	logger.warn("Order with Id:{} is alreadycanceller",orderId);
            	return AppConstants.ORDER_ALREADY_CANCELLED+orderId;
            }
        } else {
        	logger.warn("Order not found with ID: {}", orderId);
            throw new OrderNotFoundException(AppConstants.ORDER_NOT_FOUND+orderId);
        }
		
	}

	@Override
	public List<OrderDTO> viewOrdersByCustomer(Integer customerId) {
		logger.info("Fetching orders for customer ID: {}", customerId);
		List<OrderEntity> orders = orderRepository.findByCustomerId(customerId);
		
		logger.info("Fetched orders for customer ID: {}", customerId);
        return orders.stream().map(orderMapper::toOrderDTO).toList();
	}

	@Override
	public OrderDTO viewOrderById(Integer orderId) {
		logger.info("Fetching order details for order ID: {}", orderId);
		Optional<OrderEntity> orderOptional = orderRepository.findById(orderId);
        if (orderOptional.isPresent()) {
        	logger.info("Successfully Fetched order details for order ID: {}", orderId);
            return orderMapper.toOrderDTO(orderOptional.get());
        } else {
        	logger.warn("Order not found with ID: {}", orderId);
            throw new OrderNotFoundException(AppConstants.ORDER_NOT_FOUND+orderId);
        }
	}

	@Override
	@Transactional
	public OrderDTO confirmPayment(Integer orderId, String paymentId) {
		logger.info("Confirming payment for order ID: {}, payment ID: {}", orderId, paymentId);
		Optional<OrderEntity> orderOptional = orderRepository.findById(orderId);
        if (orderOptional.isPresent()) {
            OrderEntity orderEntity = orderOptional.get();
            PaymentResponseDTO paymentResponse = paymentService.confirmPayment(orderId,paymentId);

            if (AppConstants.PAYMENT_STATUS_PAID.equals(paymentResponse.getStatus())) {
                orderEntity.setOrderStatus(AppConstants.ORDER_STATUS_CONFIREMED);
                
                logger.info("confirming Payment for order ID: {}", orderId);
                orderRepository.save(orderEntity);
                logger.info("Payment confirmed and order ID: {} updated to status CONFIRMED", orderId);
                return orderMapper.toOrderDTO(orderEntity);
            } else {
            	logger.warn("Payment confirmation failed for payment ID: {}", paymentId);
                throw new PaymentConfirmationFailedException(AppConstants.PAYMENT_CONFIRMATION_FAILED+paymentId);
            }
        } else {
        	logger.error("Order not found with ID: {}", orderId);
            throw new OrderNotFoundException(AppConstants.ORDER_NOT_FOUND+orderId);
        }
	}
	
	
}
