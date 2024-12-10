package com.furnicycle.Ecommerce.Service;

import java.util.List;

import com.furnicycle.Ecommerce.DTO.CustomerDTO;
import com.furnicycle.Ecommerce.DTO.LoginDto;

public interface CustomerService {
	
	public CustomerDTO addCustomer(CustomerDTO customer);
	
	public CustomerDTO editCustomer(CustomerDTO customer);
	
	public String deleteCustomer(Integer customerId);
	
	public CustomerDTO getCustomer(Integer customerId);
	
	public CustomerDTO getCustomer(String customerName);
	
	public List<CustomerDTO> getAllCustomers();
	
	public boolean isValidCustomer(Integer customerId);
	
	public CustomerDTO login(LoginDto loginDto);
}
