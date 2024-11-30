package com.furnicycle.Ecommerce.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
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
import com.furnicycle.Ecommerce.Controller.CustomerController;
import com.furnicycle.Ecommerce.DTO.CustomerDTO;
import com.furnicycle.Ecommerce.DTO.UserDTO;
import com.furnicycle.Ecommerce.Service.CustomerService;
import com.furnicycle.Ecommerce.exception.CustomerAlreadyExistsException;
import com.furnicycle.Ecommerce.exception.CustomerNotFoundException;
import com.furnicycle.Ecommerce.exception.UserAlreadyExistsException;

@WebMvcTest(CustomerController.class)
public class CustomerControllerTest {
	
	@MockBean
	private CustomerService customerService;
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	private CustomerDTO customer1;
	private CustomerDTO customer2;
	
	private List<CustomerDTO> list;
	
	private UserDTO user1;
	private UserDTO user2;
	
	@BeforeEach
	public void setUp() {
		user1=new UserDTO(1,"swagat","Swag0810@","ADMIN");
		customer1=new CustomerDTO(1,"Swagat Jena","Bengaluru",user1);
		
		user2=new UserDTO(2,"anish","Anish08@","ADMIN");
		customer2=new CustomerDTO(2,"Anish Nayak","Hyderabad",user2);
		
		list=new ArrayList<>();
		list.add(customer1);
		list.add(customer2);
		
	}
	
	@Test
	public void findCustomerByID_Test() throws Exception {
		when(customerService.getCustomer(1)).thenReturn(customer1);
		mockMvc.perform(get("/customer/search/id/{customerId}",1))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.customerId").value(customer1.getCustomerId()))
				.andExpect(jsonPath("$.customerName").value(customer1.getCustomerName()));
		
	}
	
	@Test
	public void findCustomerByName_Test() throws Exception {
		when(customerService.getCustomer("Swagat Jena")).thenReturn(customer1);
		mockMvc.perform(get("/customer/search/name/{customerName}","Swagat Jena"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.customerId").value(customer1.getCustomerId()))
				.andExpect(jsonPath("$.customerName").value(customer1.getCustomerName()));
		
	}
	
	@Test
	public void viewAllCustomers_Test() throws Exception{
		when(customerService.getAllCustomers()).thenReturn(list);
		mockMvc.perform(get("/customer/viewall"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].customerId").value(customer1.getCustomerId()))
				.andExpect(jsonPath("$[0].customerName").value(customer1.getCustomerName()));
		
	}
	
	@Test
	public void deleteCustomer_Test() throws Exception{
		when(customerService.deleteCustomer(1)).thenReturn("customer deleted sucessfully");
		//MvcResult result=
		mockMvc.perform(delete("/customer/delete/{customerId}",1))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().string("customer deleted sucessfully"));
					
	}
	
	@Test
	public void addCustomer_Test() throws JsonProcessingException, Exception {
		when(customerService.addCustomer(any(CustomerDTO.class))).thenReturn(customer1);
		mockMvc.perform(post("/customer/add")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(customer1)))
				.andDo(print())
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.customerId").value(customer1.getCustomerId()))
				.andExpect(jsonPath("$.customerName").value(customer1.getCustomerName()));
				
				
	}
	
	@Test
	public void findCustomerById_NotFound_Test() throws Exception {
	   when(customerService.getCustomer(3))
	   	.thenThrow(new CustomerNotFoundException("No customer found with Id: "+3));
 
	    mockMvc.perform(get("/customer/search/id/{customerId}", 3))
	    		.andDo(print())
	            .andExpect(status().isNotFound())
	            .andExpect(jsonPath("$.message").value("No customer found with Id: "+3));
	}
	
	@Test
	public void findCustomerByName_NotFound_Test() throws Exception {
	   when(customerService.getCustomer("Test"))
	   	.thenThrow(new CustomerNotFoundException("No customer found with name : "+"Test"));
 
	    mockMvc.perform(get("/customer/search/name/{customerName}", "Test"))
	    		.andDo(print())
	            .andExpect(status().isNotFound())
	            .andExpect(jsonPath("$.message").value("No customer found with name : "+"Test"));
	}
	
	
	@Test
	public void deleteCustomer_NotFound_Test() throws Exception{
		when(customerService.deleteCustomer(3))
			.thenThrow(new CustomerNotFoundException("No customer found with Id: "+3));
		
		mockMvc.perform(delete("/customer/delete/{customerId}",3))
			.andDo(print())
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.message").value("No customer found with Id: "+3));
	}
	
	@Test
	public void addCustomer_CustomerExists_Test() throws Exception{
		when(customerService.addCustomer(any(CustomerDTO.class)))
			.thenThrow(new CustomerAlreadyExistsException("Customer already exists with name "
					+customer1.getCustomerName()));
		
		mockMvc.perform(post("/customer/add")
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(customer1)))
			.andDo(print())
			.andExpect(status().isConflict())
			.andExpect(jsonPath("$.message").value("Customer already exists with name "
					+customer1.getCustomerName()));
	}
	
	@Test
	public void addCustomer_UserExists_Test() throws Exception{
		when(customerService.addCustomer(any(CustomerDTO.class)))
			.thenThrow(new UserAlreadyExistsException("User already exists with Username : "
					+user1.getUsername()));
		
		mockMvc.perform(post("/customer/add")
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(customer1)))
			.andDo(print())
			.andExpect(status().isConflict())
			.andExpect(jsonPath("$.message").value("User already exists with Username : "
					+user1.getUsername()));
	}
	
	@Test
	public void validateCustomer_Test() throws Exception{
		when(customerService.isValidCustomer(anyInt())).thenReturn(true);
		mockMvc.perform(get("/customer/validate/"+1))
		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(jsonPath("$").value(true));
	}
	
	@Test
    public void testValidateCustomer_InvalidCustomer() throws Exception {
        // Mocking the customerService to return false
        Integer invalidCustomerId = 999;
        when(customerService.isValidCustomer(invalidCustomerId)).thenReturn(false);
 
        // Performing the GET request and verifying the result
        mockMvc.perform(get("/customer/validate/{customerId}", invalidCustomerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(false));
    }
 
}