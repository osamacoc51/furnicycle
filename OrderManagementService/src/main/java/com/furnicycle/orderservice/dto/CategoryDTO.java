package com.furnicycle.orderservice.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDTO {
	
	private Integer categoryId;
	
	private String categoryName;
	
	private String categoryDescription;
	
	@JsonIgnore
	private List<ProductDTO> productsDTO;
}
