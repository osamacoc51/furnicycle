package com.furnicycle.Ecommerce.controller;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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
import com.furnicycle.Ecommerce.constants.AppConstants;
import com.furnicycle.Ecommerce.controller.CartController;
import com.furnicycle.Ecommerce.dto.CartDTO;
import com.furnicycle.Ecommerce.dto.CartItemDTO;
import com.furnicycle.Ecommerce.exception.CartNotFoundException;
import com.furnicycle.Ecommerce.exception.InsufficientStockException;
import com.furnicycle.Ecommerce.exception.InvalidProductException;
import com.furnicycle.Ecommerce.service.CartService;
@WebMvcTest(CartController.class)
public class CartControllerTest {
	
	@MockBean
	private CartService cartService;
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper mapper;
	
	private CartDTO cartDTO;
	private CartItemDTO cartItem1;
	private CartItemDTO cartItem2;
	
	private List<CartItemDTO> cartItems;
	
	@BeforeEach
	public void setUp() {
		cartItem1=new CartItemDTO(1,5,2,null);
		cartItem2=new CartItemDTO(2,2,3,null);
		cartItems=new ArrayList<>();
		cartItems.add(cartItem1);
		cartItems.add(cartItem2);
		
		cartDTO=new CartDTO(1,new Date(),1,null);
	}
	
	@Test
	public void CreateCart_Test() throws JsonProcessingException, Exception {
		when(cartService.createCart(1)).thenReturn(AppConstants.CART_SUCCESSFUL+1);
		mockMvc.perform(post("/cart/create/"+1)
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(cartDTO)))
			.andDo(print())
			.andExpect(status().isCreated())
			.andExpect(content().string(AppConstants.CART_SUCCESSFUL+1));
		
		 verify(cartService, times(1)).createCart(1);
		
	}
	
 
	
	
	@Test
	public void addItemToCart_Test() throws JsonProcessingException, Exception {
		when(cartService.addItemsToCart(1, 2)).thenReturn(cartItem1);
		mockMvc.perform(post("/cart/"+1+"/add/"+2)
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(cartItem1)))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.productId").value(2))
			.andExpect(jsonPath("$.quantity").value(cartItem1.getQuantity()))
			.andExpect(jsonPath("$.cartItemId").value(cartItem1.getCartItemId()));
		
		verify(cartService, times(1)).addItemsToCart(1, 2);
	}
	
	@Test
	public void addItemToCart_InvalidProduct_Test() throws JsonProcessingException, Exception {
		when(cartService.addItemsToCart(1, 22))
		.thenThrow(new InvalidProductException(AppConstants.PRODUCT_NOTFOUND+22));
		mockMvc.perform(post("/cart/"+1+"/add/"+22)
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(cartItem1)))
			.andDo(print())
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.message").value(AppConstants.PRODUCT_NOTFOUND+22));
		
		verify(cartService, times(1)).addItemsToCart(1, 22);
	}
	
	@Test
	public void addItemToCart_CartNotFound_Test() throws JsonProcessingException, Exception {
		when(cartService.addItemsToCart(11, 2))
		.thenThrow(new CartNotFoundException(AppConstants.CART_NOTFOUND+11));
		mockMvc.perform(post("/cart/"+11+"/add/"+2)
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(cartItem1)))
			.andDo(print())
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.message").value(AppConstants.CART_NOTFOUND+11));
		
		verify(cartService, times(1)).addItemsToCart(11, 2);
	}
	
	@Test
	public void addItemToCart_InsufficientStock_Test() throws JsonProcessingException, Exception {
		when(cartService.addItemsToCart(1, 11))
		.thenThrow(new InsufficientStockException(AppConstants.INSUFFICIENT_STOCK+11));
		mockMvc.perform(post("/cart/"+1+"/add/"+11)
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(cartItem1)))
			.andDo(print())
			.andExpect(status().isConflict())
			.andExpect(jsonPath("$.message").value(AppConstants.INSUFFICIENT_STOCK+11));
		
		verify(cartService, times(1)).addItemsToCart(1, 11);
	}
	
	
	@Test
	public void removeItemFromCart_Test() throws JsonProcessingException, Exception {
		when(cartService.removeItemsFromCart(1, 2)).thenReturn(cartItem1);
		mockMvc.perform(delete("/cart/"+1+"/remove/"+2)
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(cartItem1)))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.productId").value(2))
			.andExpect(jsonPath("$.quantity").value(cartItem1.getQuantity()))
			.andExpect(jsonPath("$.cartItemId").value(cartItem1.getCartItemId()));
		
		verify(cartService, times(1)).removeItemsFromCart(1, 2);
	}
	
	@Test
	public void removeItemFromCart_InvalidProduct_Test() throws JsonProcessingException, Exception {
		when(cartService.removeItemsFromCart(1, 22))
		.thenThrow(new InvalidProductException(AppConstants.PRODUCT_NOTFOUND+22));
		mockMvc.perform(delete("/cart/"+1+"/remove/"+22)
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(cartItem1)))
			.andDo(print())
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.message").value(AppConstants.PRODUCT_NOTFOUND+22));
		
		verify(cartService, times(1)).removeItemsFromCart(1, 22);
	}
 
	@Test
	public void removeItemFromCart_CartNotFound_Test() throws JsonProcessingException, Exception {
		when(cartService.removeItemsFromCart(11, 2))
		.thenThrow(new CartNotFoundException(AppConstants.CART_NOTFOUND+11));
		mockMvc.perform(delete("/cart/"+11+"/remove/"+2)
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(cartItem1)))
			.andDo(print())
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.message").value(AppConstants.CART_NOTFOUND+11));
		
		verify(cartService, times(1)).removeItemsFromCart(11, 2);
	}
	
	
	
	@Test
    void ModifyItemQuantity_Test() throws Exception {
        cartItem1.setQuantity(5);
        when(cartService.modifyQuantity(1, 2, 5)).thenReturn(cartItem1);
 
        mockMvc.perform(put("/cart/1/modify/2/5")
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(mapper.writeValueAsString(cartItem1)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.quantity").value(5));
 
        verify(cartService, times(1)).modifyQuantity(1, 2, 5);
    }
 
	@Test
    void ModifyItemQuantity_CartNotFoundException() throws Exception {
        cartItem1.setQuantity(5);
        when(cartService.modifyQuantity(11, 2, 5))
        .thenThrow(new CartNotFoundException(AppConstants.CART_NOTFOUND+11));
 
        mockMvc.perform(put("/cart/11/modify/2/5")
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(mapper.writeValueAsString(cartItem1)))
               .andExpect(status().isNotFound())
               .andExpect(jsonPath("$.message").value(AppConstants.CART_NOTFOUND+11));
 
        verify(cartService, times(1)).modifyQuantity(11, 2, 5);
    }
	
	@Test
    void testModifyItemQuantity_InvalidProductException() throws Exception {
        cartItem1.setQuantity(5);
        when(cartService.modifyQuantity(1, 22, 5))
        .thenThrow(new InvalidProductException(AppConstants.PRODUCT_NOTFOUND+22));
 
        mockMvc.perform(put("/cart/1/modify/22/5")
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(mapper.writeValueAsString(cartItem1)))
               .andExpect(status().isNotFound())
               .andExpect(jsonPath("$.message").value(AppConstants.PRODUCT_NOTFOUND+22));
 
        verify(cartService, times(1)).modifyQuantity(1, 22, 5);
    }
	
	@Test
    void testModifyItemQuantity_InsufficientStock() throws Exception {
        cartItem1.setQuantity(5);
        when(cartService.modifyQuantity(1, 22, 5))
        .thenThrow(new InsufficientStockException(AppConstants.INSUFFICIENT_STOCK+22));
 
        mockMvc.perform(put("/cart/1/modify/22/5")
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(mapper.writeValueAsString(cartItem1)))
               .andExpect(status().isConflict())
               .andExpect(jsonPath("$.message").value(AppConstants.INSUFFICIENT_STOCK+22));
 
        verify(cartService, times(1)).modifyQuantity(1, 22, 5);
    }
	
	@Test
    void ClearCart_Test() throws Exception {
        when(cartService.clearCart(1)).thenReturn(AppConstants.CLEAR_CART);
 
        mockMvc.perform(delete("/cart/1/clear")
        		.contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(content().string(AppConstants.CLEAR_CART));
 
        verify(cartService, times(1)).clearCart(1);
    }
	
	@Test
    void ClearCart_CartNotFoundException_Test() throws Exception {
        when(cartService.clearCart(11))
        .thenThrow(new CartNotFoundException(AppConstants.CART_NOTFOUND+11));
 
        mockMvc.perform(delete("/cart/11/clear")
        		.contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isNotFound())
               .andExpect(jsonPath("$.message").value(AppConstants.CART_NOTFOUND+11));
 
        verify(cartService, times(1)).clearCart(11);
    }
	
	@Test
    void GetCartByCustomerId_test() throws Exception {
        when(cartService.getCartByCustomerId(1)).thenReturn(cartDTO);
 
        mockMvc.perform(get("/cart/customer/1"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.cartId").value(cartDTO.getCartId()))
               .andExpect(jsonPath("$.customerId").value(1));
 
        verify(cartService, times(1)).getCartByCustomerId(1);
    }
	
	@Test
    void GetCartByCustomerId_CartNotFoundException_test() throws Exception {
        when(cartService.getCartByCustomerId(11))
        .thenThrow(new CartNotFoundException(AppConstants.CART_NOTFOUND_FOR_CUSTOMER+11));
 
        mockMvc.perform(get("/cart/customer/11"))
               .andExpect(status().isNotFound())
               .andExpect(jsonPath("$.message").value(AppConstants.CART_NOTFOUND_FOR_CUSTOMER+11));
 
        verify(cartService, times(1)).getCartByCustomerId(11);
    }
	
	@Test
    void GetCartById_Test() throws Exception {
        when(cartService.getCartById(1)).thenReturn(cartDTO);
 
        mockMvc.perform(get("/cart/1"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.cartId").value(1))
               .andExpect(jsonPath("$.customerId").value(cartDTO.getCustomerId()));
 
        verify(cartService, times(1)).getCartById(1);
    }
	
	@Test
    void GetCartById_CartNotFoundException_Test() throws Exception {
        when(cartService.getCartById(11))
        .thenThrow(new CartNotFoundException(AppConstants.CART_NOTFOUND+11));
 
        mockMvc.perform(get("/cart/11"))
               .andExpect(status().isNotFound())
               .andExpect(jsonPath("$.message").value(AppConstants.CART_NOTFOUND+11));
               
 
        verify(cartService, times(1)).getCartById(11);
    }
	
	
 
}