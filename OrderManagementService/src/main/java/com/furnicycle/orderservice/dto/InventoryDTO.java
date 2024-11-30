package com.furnicycle.orderservice.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InventoryDTO {
	private Integer inventoryId;
	
	private Integer productId;
	
	private Integer availableStock;
	
	private Integer reservedStock;
	
	private Date lastUpdated;
}
