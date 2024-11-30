package com.furnicycle.orderservice.repositorytest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.furnicycle.orderservice.constants.AppConstants;
import com.furnicycle.orderservice.entity.OrderEntity;
import com.furnicycle.orderservice.repository.OrderRepository;

@DataJpaTest
public class OrderRepositoryTest {
	@Autowired
    private OrderRepository orderRepository;

    private OrderEntity order1;
    private OrderEntity order2;

    @BeforeEach
    void setUp() {
        // Initialize and save two test orders for the same customer
        order1 = new OrderEntity();
        order1.setCustomerId(1);
        order1.setOrderDate(new Date());
        order1.setOrderStatus(AppConstants.ORDER_STATUS_PENDING);
        order1.setTotalAmount(100.0);
        order1.setCartId(1);
        
        order2 = new OrderEntity();
        order2.setCustomerId(1);
        order2.setOrderDate(new Date());
        order2.setOrderStatus(AppConstants.ORDER_STATUS_CONFIREMED);
        order2.setTotalAmount(200.0);
        order2.setCartId(2);

        orderRepository.save(order1);
        orderRepository.save(order2);
    }

    @Test
    void testFindByCustomerId() {
        
        List<OrderEntity> orders = orderRepository.findByCustomerId(1);

        assertEquals(2, orders.size());  // Expecting 2 orders for customerId 1
        assertEquals(order1.getOrderId(), orders.get(0).getOrderId());
        assertEquals(order2.getOrderId(), orders.get(1).getOrderId());
    }

    @Test
    void testFindByCustomerId_NoOrders() {
      
        List<OrderEntity> orders = orderRepository.findByCustomerId(2);
        assertTrue(orders.isEmpty()); 
    }
}
