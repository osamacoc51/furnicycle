package com.furnicycle.Ecommerce.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDTO {
	private Integer customerId;
	
	private String customerName;
	
	private String address;
	
	private UserDTO userDTO;
}
