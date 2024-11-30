package com.furnicycle.Ecommerce.dto;

import java.util.Date;
import java.util.List;



import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartDTO {
	
	private Integer cartId;
	
    private Date creationDate;
    
    private Integer customerId;
    
    
    private List<CartItemDTO> cartItemDtos;

}
