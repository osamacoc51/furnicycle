package com.furnicycle.orderservice.serviceImpltest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;


import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import com.furnicycle.orderservice.constants.AppConstants;
import com.furnicycle.orderservice.dto.OrderDTO;
import com.furnicycle.orderservice.dto.PaymentResponseDTO;
import com.furnicycle.orderservice.entity.OrderEntity;
import com.furnicycle.orderservice.exception.PaymentConfirmationFailedException;
import com.furnicycle.orderservice.repository.OrderRepository;
import com.furnicycle.orderservice.service.DummyPaymentServiceImpl;

@SpringBootTest
public class DummyPaymentServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private DummyPaymentServiceImpl dummyPaymentService;

   

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreatePayment() {
        // Given
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setOrderId(1);
        orderDTO.setTotalAmount(1000.0);

        // When
        PaymentResponseDTO response = dummyPaymentService.createPayment(orderDTO);

        // Then
        assertNotNull(response);
        assertEquals("INR", response.getCurrency());
        assertEquals(1000.0, response.getAmount());
        assertEquals(AppConstants.PAYMENT_STATUS_CREATED, response.getStatus());
        assertNotNull(response.getPaymentId());

        
    }

    @Test
    public void testConfirmPayment() {
        // Given
        int orderId = 1;
        String paymentId = UUID.randomUUID().toString();

        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setOrderId(orderId);
        orderEntity.setTotalAmount(1000.0);

        // Mocking repository response
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(orderEntity));

        // When
        PaymentResponseDTO response = dummyPaymentService.confirmPayment(orderId, paymentId);

        // Then
        assertNotNull(response);
        assertEquals(paymentId, response.getPaymentId());
        assertEquals("INR", response.getCurrency());
        assertEquals(1000.0, response.getAmount());
        assertEquals(AppConstants.PAYMENT_STATUS_PAID, response.getStatus());

       
    }

    @Test
    public void testConfirmPaymentOrderNotFound() {
        // Given
        int orderId = 1;
        String paymentId = UUID.randomUUID().toString();

        // Mock repository to return an empty Optional
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        // Expecting an exception when order is not found
        assertThrows(PaymentConfirmationFailedException.class, () -> {
            dummyPaymentService.confirmPayment(orderId, paymentId);
        });

        
    }
}
