package com.furnicycle.orderservice.serviceImpltest;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.furnicycle.orderservice.constants.AppConstants;
import com.furnicycle.orderservice.dto.CartDTO;
import com.furnicycle.orderservice.dto.CartItemDTO;
import com.furnicycle.orderservice.dto.InventoryDTO;
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
import com.furnicycle.orderservice.service.OrderServiceImpl;
import com.furnicycle.orderservice.service.PaymentService;



@ExtendWith(MockitoExtension.class)
public class orderServiceImplTest {
 
	@Mock
	private OrderRepository orderRepository;
	
	@Mock
	private OrderMapper orderMapper;
	
	@Mock
	private CustomerServiceClient customerServiceClient;
	
	@Mock
	private ProductServiceClient productServiceClient;
	
	@Mock
	private CartServiceClient cartServiceClient;
	
	@Mock
	private PaymentService paymentService;
	
	@Mock
	private InventoryServiceClient inventoryServiceClient;
	
	@InjectMocks
	private OrderServiceImpl orderService;
	
	OrderEntity orderEntity;
	OrderDTO orderDTO;
	
	private CartDTO cartDTO;
	private CartItemDTO cartItemDTO;
	private PaymentResponseDTO paymentResponseDTO;
	
	private ProductDTO productDTO;
	
	List<OrderEntity> orderEntityList;
	List<OrderDTO> orderDTOList;
	
	@BeforeEach
	public void setUp() {
		
		orderEntity=new OrderEntity(1,1,new Date(),"PENDING",19999.0,1);
		orderDTO=new OrderDTO(1,1,new Date(),"PENDING",19999.0,1);
		
		cartItemDTO=new CartItemDTO();
		cartItemDTO.setCartItemId(1);
		cartItemDTO.setProductId(1);
		cartItemDTO.setQuantity(4);
		
		List<CartItemDTO> list=new ArrayList<>();
		list.add(cartItemDTO);
		
		cartDTO=new CartDTO();
		cartDTO.setCartId(1);
		cartDTO.setCreationDate(new Date());
		cartDTO.setCustomerId(1);
		cartDTO.setCartItemDtos(list);
		
		productDTO=new ProductDTO(1,"Table","6 seater table",19999.0,5, null);
		
		paymentResponseDTO=new PaymentResponseDTO("zz123",19999.0,"Rupee",AppConstants.PAYMENT_STATUS_CREATED);
		
		orderEntityList=new ArrayList<>();
		orderEntityList.add(orderEntity);
		
		orderDTOList=new ArrayList<>();
		orderDTOList.add(orderDTO);
	}
	
	@AfterEach
	public void tearDown() {
		orderEntity=null;
		orderDTO=null;
		paymentResponseDTO=null;
		cartDTO=null;
		cartItemDTO=null;
		productDTO=null;
	}
	
	@Test
	public void createOrder_Success_Test() {
		when(customerServiceClient.isValidCustomer(anyInt())).thenReturn(true);
		when(cartServiceClient.getCartById(anyInt())).thenReturn(Optional.of(cartDTO));
		when(productServiceClient.getProductById(anyInt())).thenReturn(Optional.of(productDTO));
		when(orderRepository.save(any(OrderEntity.class))).thenReturn(orderEntity);
		when(orderMapper.toOrderDTO(any(OrderEntity.class))).thenReturn(orderDTO);
		when(paymentService.createPayment(any(OrderDTO.class))).thenReturn(paymentResponseDTO);
		when(inventoryServiceClient.reduceReservedStockAfterOrder(anyInt(), anyInt()))
		.thenReturn(new InventoryDTO(1,1,5,4,null));
		
		OrderDTO result = orderService.createOrder(1, 1);
		
		assertNotNull(result);
        assertEquals(1, result.getOrderId());
        assertEquals(19999.0, result.getTotalAmount());
        verify(customerServiceClient, times(1)).isValidCustomer(1);
        verify(cartServiceClient, times(2)).getCartById(1);
        verify(productServiceClient, times(1)).getProductById(1);
        verify(orderRepository, times(1)).save(any(OrderEntity.class));
        verify(paymentService, times(1)).createPayment(orderDTO);
        verify(inventoryServiceClient,times(1)).reduceReservedStockAfterOrder(1, 4);
		
	}
	
	@Test
	public void createOrder_Success_PaymentFailed_Test() {
		when(customerServiceClient.isValidCustomer(anyInt())).thenReturn(true);
		when(cartServiceClient.getCartById(anyInt())).thenReturn(Optional.of(cartDTO));
		when(productServiceClient.getProductById(anyInt())).thenReturn(Optional.of(productDTO));
		when(orderRepository.save(any(OrderEntity.class))).thenReturn(orderEntity);
		when(orderMapper.toOrderDTO(any(OrderEntity.class))).thenReturn(orderDTO);
		
		PaymentResponseDTO paymentDTO = new PaymentResponseDTO();
        paymentDTO.setStatus("FAILED");
        
		when(paymentService.createPayment(any(OrderDTO.class))).thenReturn(paymentDTO);
		
		
		assertThrows(PaymentFailedException.class, () -> {
            orderService.createOrder(1, 1); // customerId = 1, cartId = 1
        });
		
		 verify(orderRepository, times(1)).save(any(OrderEntity.class));
	     verify(paymentService, times(1)).createPayment(any(OrderDTO.class));
		
	}
	
	@Test
	public void CreateOrder_CustomerNOTFOUND() {
		when(customerServiceClient.isValidCustomer(anyInt())).thenReturn(false);
		assertThatThrownBy(()->orderService.createOrder(33, 33))
		.isInstanceOf(CustomerNotFoundException.class)
		.hasMessage(AppConstants.CUSTOMER_NOT_FOUND+33);
		
		verify(orderRepository, never()).save(any(OrderEntity.class));
	}
	
	@Test
	public void CreateOrder_CartNOTFOUND() {
		when(customerServiceClient.isValidCustomer(anyInt())).thenReturn(true);
		when(cartServiceClient.getCartById(anyInt())).thenReturn(Optional.empty());
		assertThatThrownBy(()->orderService.createOrder(33, 33))
		.isInstanceOf(CartNotFoundException.class)
		.hasMessage(AppConstants.CART_NOT_FOUND+33);
		
		verify(orderRepository, never()).save(any(OrderEntity.class));
	}
	
	@Test
	public void createOrder_ProductNotFound_ThrowsException() {
	    when(customerServiceClient.isValidCustomer(anyInt())).thenReturn(true);
	    
	    CartDTO mockCart = new CartDTO();
	    mockCart.setCartId(1);
	    mockCart.setCustomerId(1);
	    mockCart.setCartItemDtos(List.of(new CartItemDTO(1, 1, 3,null)));  
	    when(cartServiceClient.getCartById(anyInt())).thenReturn(Optional.of(mockCart));
	    
	    when(productServiceClient.getProductById(anyInt())).thenThrow(new ProductNotFoundException(AppConstants.PRODUCT_NOT_FOUND + 1));
	    
	  
	    assertThatThrownBy(()->orderService.createOrder(1, 1))
	    .isInstanceOf(ProductNotFoundException.class)
	    .hasMessage(AppConstants.PRODUCT_NOT_FOUND+1);
	    
	    verify(orderRepository, never()).save(any(OrderEntity.class));
	}
	
	@Test
	public void cancelOrder_Success() {
		when(orderRepository.findById(anyInt())).thenReturn(Optional.of(orderEntity));
		
		 CartItemDTO cartItem = new CartItemDTO();
		 cartItem.setProductId(1);
		 cartItem.setQuantity(2);
 
		 CartDTO cartDTO = new CartDTO();
		 cartDTO.setCartId(1);
		 cartDTO.setCustomerId(1);
		 cartDTO.setCartItemDtos(List.of(cartItem));
		
		when(cartServiceClient.getCartById(anyInt()))
		.thenReturn(Optional.of(cartDTO));
		
		when(inventoryServiceClient.increaseAvailableStockAfterOrderCancel(anyInt(), anyInt()))
		.thenReturn(new InventoryDTO(1,1,5,0,null));
		
		String response=orderService.cancelOrder(1);
		assertEquals(response,AppConstants.CANCEL_ORDER);
		
		verify(orderRepository,times(1)).findById(1);
		verify(orderRepository,times(1)).save(any(OrderEntity.class));
	}
	
	@Test
	public void cancelOrder_AlreadyCanceled() {
	    OrderEntity canceledOrderEntity = new OrderEntity();
	    canceledOrderEntity.setOrderId(1);
	    canceledOrderEntity.setOrderStatus(AppConstants.ORDER_STATUS_CANCELED);
	    canceledOrderEntity.setCartId(1);
 
	    when(orderRepository.findById(anyInt())).thenReturn(Optional.of(canceledOrderEntity));
 
	    String response = orderService.cancelOrder(1);
	    assertEquals(AppConstants.ORDER_ALREADY_CANCELLED+1, response);
 
	    verify(orderRepository, times(0)).save(any(OrderEntity.class));
	    verify(inventoryServiceClient, times(0)).increaseAvailableStockAfterOrderCancel(anyInt(), anyInt());
	}
	
	@Test
	public void cancelOrder_OrderNotFound() {
		when(orderRepository.findById(1)).thenReturn(Optional.empty());
		assertThatThrownBy(()->orderService.cancelOrder(1))
		.isInstanceOf(OrderNotFoundException.class)
		.hasMessage(AppConstants.ORDER_NOT_FOUND+1);
	}
	
	@Test
	public void viewOrdersByCustomer() {
		when(orderRepository.findByCustomerId(1)).thenReturn(orderEntityList);
		when(orderMapper.toOrderDTO(orderEntity)).thenReturn(orderDTO);
		
		List<OrderDTO> result=orderService.viewOrdersByCustomer(1);
		
		assertNotNull(result);
		assertEquals(result.get(0).getOrderId(), orderDTO.getOrderId());
		assertEquals(result.get(0).getCartId(), orderDTO.getCartId());
		assertEquals(result.get(0).getCustomerId(), orderDTO.getCustomerId());
		assertEquals(result.get(0).getOrderStatus(), orderDTO.getOrderStatus());
		
		verify(orderRepository,times(1)).findByCustomerId(1);
	}
	
	@Test
	public void viewOrdersByOrderId() {
		when(orderRepository.findById(1)).thenReturn(Optional.of(orderEntity));
		when(orderMapper.toOrderDTO(orderEntity)).thenReturn(orderDTO);
		
		OrderDTO result=orderService.viewOrderById(1);
		
		assertNotNull(result);
		assertEquals(result.getOrderId(), orderDTO.getOrderId());
		assertEquals(result.getCartId(), orderDTO.getCartId());
		assertEquals(result.getCustomerId(), orderDTO.getCustomerId());
		assertEquals(result.getOrderStatus(), orderDTO.getOrderStatus());
		
		
		verify(orderRepository,times(1)).findById(1);
	}
	
	@Test
	public void viewOrdersByOrderId_OrderNotFoundException() {
		when(orderRepository.findById(1)).thenReturn(Optional.empty());
		assertThatThrownBy(()->orderService.viewOrderById(1))
		.isInstanceOf(OrderNotFoundException.class)
		.hasMessage(AppConstants.ORDER_NOT_FOUND+1);
	}
	
	@Test
	public void confirmPayment_Test() {
		paymentResponseDTO.setStatus(AppConstants.PAYMENT_STATUS_PAID);
		orderDTO.setOrderStatus(AppConstants.ORDER_STATUS_CONFIREMED);
		when(orderRepository.findById(1)).thenReturn(Optional.of(orderEntity));
		when(paymentService.confirmPayment(1, "z12x")).thenReturn(paymentResponseDTO);
		when(orderMapper.toOrderDTO(orderEntity)).thenReturn(orderDTO);
		
		OrderDTO result = orderService.confirmPayment(1, "z12x");
 
        // Assert
        assertNotNull(result);
        assertEquals(AppConstants.ORDER_STATUS_CONFIREMED, result.getOrderStatus());
        assertEquals(result.getOrderId(),orderDTO.getOrderId());
        assertEquals(result.getCartId(),orderDTO.getCartId());
        assertEquals(result.getCustomerId(),orderDTO.getCustomerId());
        verify(orderRepository, times(1)).save(any(OrderEntity.class));
	}
	
	@Test
    public void testConfirmPayment_OrderNotFound() {
        // Arrange
        when(orderRepository.findById(1)).thenReturn(Optional.empty());
 
        // Act & Assert
        OrderNotFoundException thrown = assertThrows(OrderNotFoundException.class, () -> {
            orderService.confirmPayment(1, "paymentId123");
        });
        assertEquals(AppConstants.ORDER_NOT_FOUND + 1, thrown.getMessage());
    }
	
	@Test
    public void testConfirmPayment_PaymentFailed() {
        // Arrange
        PaymentResponseDTO failedPaymentResponse = new PaymentResponseDTO();
        failedPaymentResponse.setPaymentId("paymentId123");
        failedPaymentResponse.setAmount(100.0);
        failedPaymentResponse.setCurrency("INR");
        failedPaymentResponse.setStatus(AppConstants.PAYMENT_CONFIRMATION_FAILED);
        
        when(orderRepository.findById(1)).thenReturn(Optional.of(orderEntity));
        when(paymentService.confirmPayment(1, "paymentId123")).thenReturn(failedPaymentResponse);
 
        // Act & Assert
        PaymentConfirmationFailedException thrown = assertThrows(PaymentConfirmationFailedException.class, () -> {
            orderService.confirmPayment(1, "paymentId123");
        });
        assertEquals(AppConstants.PAYMENT_CONFIRMATION_FAILED + "paymentId123", thrown.getMessage());
    }
}