package com.furnicycle.orderservice.dto;



import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
	private Integer productId;
	
	private String productName;
	
	private String productDescription;
	
	private Double productPrice;
	
	private Integer stock;
	
	private CategoryDTO categoryDTO;
}
