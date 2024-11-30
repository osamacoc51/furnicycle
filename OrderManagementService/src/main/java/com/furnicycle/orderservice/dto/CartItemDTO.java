package com.furnicycle.orderservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartItemDTO {
	private Integer cartItemId;
	
	private Integer quantity;
	
	private Integer productId;
	
	@JsonIgnore
	private CartDTO cartDTO;
}
