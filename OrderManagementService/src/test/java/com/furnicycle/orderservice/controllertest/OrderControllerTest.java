package com.furnicycle.orderservice.controllertest;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.furnicycle.orderservice.constants.AppConstants;
import com.furnicycle.orderservice.controller.OrderController;
import com.furnicycle.orderservice.dto.OrderDTO;
import com.furnicycle.orderservice.entity.OrderEntity;
import com.furnicycle.orderservice.exception.CartNotFoundException;
import com.furnicycle.orderservice.exception.CustomerNotFoundException;
import com.furnicycle.orderservice.exception.OrderNotFoundException;
import com.furnicycle.orderservice.exception.PaymentConfirmationFailedException;
import com.furnicycle.orderservice.exception.PaymentFailedException;
import com.furnicycle.orderservice.exception.ProductNotFoundException;
import com.furnicycle.orderservice.service.OrderService;
import com.furnicycle.orderservice.service.PaymentService;

@WebMvcTest(OrderController.class)
public class OrderControllerTest {
	
	@MockBean
	private OrderService orderService;
	
	@MockBean
	private PaymentService paymentService;
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	
	OrderEntity orderEntity;
	OrderDTO orderDTO;
	
	List<OrderEntity> orderEntityList;
	List<OrderDTO> orderDTOList;
	
	@BeforeEach
	public void setup() {
		
		orderEntity=new OrderEntity(1,1,new Date(),"PENDING",19999.0,1);
		orderDTO=new OrderDTO(1,1,new Date(),"PENDING",19999.0,1);
		
		orderEntityList=new ArrayList<>();
		orderEntityList.add(orderEntity);
		
		orderDTOList=new ArrayList<>();
		orderDTOList.add(orderDTO);
	}
	
	@Test
	public void createOrder_Success_Test() throws JsonProcessingException, Exception {
		when(orderService.createOrder(1, 1)).thenReturn(orderDTO);
		mockMvc.perform(post("/orders/create?customerId=1&cartId=1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(orderDTO)))
				.andDo(print())
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.orderId").value(orderDTO.getOrderId()))
				.andExpect(jsonPath("$.cartId").value(orderDTO.getCartId()));
	}
	
	@Test
	public void createOrder_CustomerNotFound_Test() throws JsonProcessingException, Exception {
		when(orderService.createOrder(11, 1))
		.thenThrow(new CustomerNotFoundException(AppConstants.CUSTOMER_NOT_FOUND+11));
		mockMvc.perform(post("/orders/create?customerId=11&cartId=1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(orderDTO)))
				.andDo(print())
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.message").value(AppConstants.CUSTOMER_NOT_FOUND+11));
	}
	
	@Test
	public void createOrder_CartNotFound_Test() throws JsonProcessingException, Exception {
		when(orderService.createOrder(1, 11))
		.thenThrow(new CartNotFoundException(AppConstants.CART_NOT_FOUND+11));
		mockMvc.perform(post("/orders/create?customerId=1&cartId=11")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(orderDTO)))
				.andDo(print())
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.message").value(AppConstants.CART_NOT_FOUND+11));
	}
	@Test
	public void createOrder_PaymentFailed_Test() throws JsonProcessingException, Exception {
		when(orderService.createOrder(1, 1))
			.thenThrow(new PaymentFailedException(AppConstants.PAYMENT_FAILED));
		mockMvc.perform(post("/orders/create?customerId=1&cartId=1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(orderDTO)))
				.andDo(print())
				.andExpect(status().isPaymentRequired())
				.andExpect(jsonPath("$.message").value(AppConstants.PAYMENT_FAILED));
	}
	
	@Test
	public void createOrder_ProductNotFound_Test() throws JsonProcessingException, Exception {
		when(orderService.createOrder(1, 1))
			.thenThrow(new ProductNotFoundException(AppConstants.PRODUCT_NOT_FOUND+1));
		mockMvc.perform(post("/orders/create?customerId=1&cartId=1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(orderDTO)))
				.andDo(print())
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.message").value(AppConstants.PRODUCT_NOT_FOUND+1));
	}
	
	@Test
	public void confirmPayment_Success_Test() throws JsonProcessingException, Exception {
		when(orderService.confirmPayment( 1,"payment1"))
		.thenReturn(orderDTO);
		
		mockMvc.perform(post("/orders/confirmPayment?orderId=1&paymentId=payment1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(orderDTO)))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.orderId").value(1))
				.andExpect(jsonPath("$.customerId").value(orderDTO.getCustomerId()))
				.andExpect(jsonPath("$.cartId").value(orderDTO.getCartId()));
				
	}
	
	@Test
	public void confirmPayment_PaymentConfirmationFailed_Test() throws JsonProcessingException, Exception {
		when(orderService.confirmPayment( 1,"payment1"))
		.thenThrow(new PaymentConfirmationFailedException(AppConstants.PAYMENT_CONFIRMATION_FAILED+"payment1"));
		
		mockMvc.perform(post("/orders/confirmPayment?orderId=1&paymentId=payment1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(orderDTO)))
				.andDo(print())
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value(AppConstants.PAYMENT_CONFIRMATION_FAILED+"payment1"));
				
	}
	
	@Test
	public void confirmPayment_OrderNotFound_Test() throws JsonProcessingException, Exception {
		when(orderService.confirmPayment( 1,"payment1"))
		.thenThrow(new OrderNotFoundException(AppConstants.ORDER_NOT_FOUND+1));
		
		mockMvc.perform(post("/orders/confirmPayment?orderId=1&paymentId=payment1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(orderDTO)))
				.andDo(print())
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.message").value(AppConstants.ORDER_NOT_FOUND+1));
				
	}
	
	@Test
	public void cancelOrder_Test() throws Exception {
		when(orderService.cancelOrder(1)).thenReturn(AppConstants.CANCEL_ORDER);
		
		mockMvc.perform(put("/orders/cancel/1"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().string(AppConstants.CANCEL_ORDER));
	}
	
	@Test
	public void cancelOrder_AlreadyCancelled_Test() throws Exception {
		when(orderService.cancelOrder(1)).thenReturn(AppConstants.ORDER_ALREADY_CANCELLED+1);
		
		mockMvc.perform(put("/orders/cancel/1"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().string(AppConstants.ORDER_ALREADY_CANCELLED+1));
	}
	
	@Test
	public void cancelOrder_OrderNotFound_Test() throws Exception {
		when(orderService.cancelOrder(1))
		.thenThrow(new OrderNotFoundException(AppConstants.ORDER_NOT_FOUND+1));
		
		mockMvc.perform(put("/orders/cancel/1"))
				.andDo(print())
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.message").value(AppConstants.ORDER_NOT_FOUND+1));
	}
	
	@Test
	public void viewOrderByCustomer_Test() throws Exception{
		when(orderService.viewOrdersByCustomer(1))
		.thenReturn(orderDTOList);
		
		mockMvc.perform(get("/orders/customer/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(orderDTO)))
				.andDo(print())
				.andExpect(jsonPath("$[0].orderId").value(1))
				.andExpect(jsonPath("$[0].customerId").value(orderDTO.getCustomerId()))
				.andExpect(jsonPath("$[0].cartId").value(orderDTO.getCartId()));
	}
	
	@Test
	public void viewOrderById_Test() throws JsonProcessingException, Exception {
		when(orderService.viewOrderById(1))
		.thenReturn(orderDTO);
		
		mockMvc.perform(get("/orders/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(orderDTO)))
				.andDo(print())
				.andExpect(jsonPath("$.orderId").value(1))
				.andExpect(jsonPath("$.customerId").value(orderDTO.getCustomerId()))
				.andExpect(jsonPath("$.cartId").value(orderDTO.getCartId()));
		
	}
	
	@Test
	public void viewOrderById_OrderNotFound_Test() throws JsonProcessingException, Exception {
		when(orderService.viewOrderById(1))
		.thenThrow(new OrderNotFoundException(AppConstants.ORDER_NOT_FOUND+1));
		
		mockMvc.perform(get("/orders/1"))
				.andDo(print())
				.andExpect(jsonPath("$.message").value(AppConstants.ORDER_NOT_FOUND+1));
		
	}
}