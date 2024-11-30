package com.furnicycle.Ecommerce.service;

import com.furnicycle.Ecommerce.dto.CartDTO;
import com.furnicycle.Ecommerce.dto.CartItemDTO;

public interface CartService {
	
	public String createCart(Integer customerId);
	
	public CartItemDTO addItemsToCart(Integer cartId, Integer productId);
	
	public CartItemDTO removeItemsFromCart(Integer cartId, Integer productId);
	
	public CartItemDTO modifyQuantity(Integer cartId, Integer productId,Integer quantity);
	
	public String clearCart(Integer cartId);
	
	public CartDTO getCartByCustomerId(Integer customerId);
	
	public CartDTO getCartById(Integer cartId);
	
}
