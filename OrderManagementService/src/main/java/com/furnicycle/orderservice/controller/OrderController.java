package com.furnicycle.orderservice.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.furnicycle.orderservice.dto.OrderDTO;
import com.furnicycle.orderservice.service.OrderService;

@RestController
@RequestMapping("/orders")
public class OrderController {
	private static final Logger logger = LogManager.getLogger(OrderController.class);

    @Autowired
    private OrderService orderService;

    @PostMapping("/create")
    public ResponseEntity<OrderDTO> createOrder(@RequestParam Integer customerId, @RequestParam Integer cartId) {
        logger.info("Received request to create order for customer ID: {} and cart ID: {}", customerId, cartId);
        OrderDTO orderDTO = orderService.createOrder(customerId, cartId);
        return ResponseEntity.status(HttpStatus.CREATED)
        						.contentType(MediaType.APPLICATION_JSON)
        						.body(orderDTO);
    }

    @PostMapping("/confirmPayment")
    public ResponseEntity<OrderDTO> confirmPayment(@RequestParam Integer orderId, @RequestParam String paymentId) {
        logger.info("Received request to confirm payment for order ID: {} with payment ID: {}", orderId, paymentId);
        OrderDTO orderDTO = orderService.confirmPayment(orderId, paymentId);
        return ResponseEntity.status(HttpStatus.OK)
        		.contentType(MediaType.APPLICATION_JSON)
        		.body(orderDTO);
    }

    @PutMapping("/cancel/{orderId}")
    public ResponseEntity<String> cancelOrder(@PathVariable Integer orderId) {
        logger.info("Received request to cancel order ID: {}", orderId);
        String response=orderService.cancelOrder(orderId);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<OrderDTO>> viewOrdersByCustomer(@PathVariable Integer customerId) {
        logger.info("Received request to fetch orders for customer ID: {}", customerId);
        List<OrderDTO> orders = orderService.viewOrdersByCustomer(customerId);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDTO> viewOrderById(@PathVariable Integer orderId) {
        logger.info("Received request to fetch order details for order ID: {}", orderId);
        OrderDTO orderDTO = orderService.viewOrderById(orderId);
        return new ResponseEntity<>(orderDTO, HttpStatus.OK);
    }
}
