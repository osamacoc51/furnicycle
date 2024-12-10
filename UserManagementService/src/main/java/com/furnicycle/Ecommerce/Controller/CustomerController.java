package com.furnicycle.Ecommerce.Controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.furnicycle.Ecommerce.DTO.CustomerDTO;
import com.furnicycle.Ecommerce.DTO.LoginDto;
import com.furnicycle.Ecommerce.Service.CustomerService;


@RestController
@RequestMapping("/customer")
public class CustomerController {
	
	@Autowired
	private CustomerService customerService;
	
	private static final Logger logger=LogManager.getLogger(CustomerController.class);
	
	@PostMapping("/add")
	public ResponseEntity<CustomerDTO> addCustomer(@RequestBody CustomerDTO customer){
		logger.info("Received request to add a new customer: {}", customer);
		CustomerDTO customerDTO=customerService.addCustomer(customer);
		logger.info("Successfully added new customer with ID: {"+ customerDTO.getCustomerId()+"}");
		return  ResponseEntity.status(HttpStatus.CREATED)
				//.contentType(MediaType.APPLICATION_JSON)
				.body(customerDTO);
				
	}
	
	@DeleteMapping("/delete/{customerId}")
	public ResponseEntity<String> deleteCustomer(@PathVariable Integer customerId){
		logger.info("Received request to delete customer with ID: {"+ customerId+"}");
		String msg=customerService.deleteCustomer(customerId);
		logger.info("Successfully deleted customer with ID: {"+ customerId+"}");
		return new ResponseEntity<>(msg, HttpStatus.OK);
	}
	
	@GetMapping("/search/id/{customerId}")
	public ResponseEntity<CustomerDTO> findCustomer(@PathVariable Integer customerId){
		logger.info("Received request to find customer by ID: {"+ customerId+"}");
		CustomerDTO customer=customerService.getCustomer(customerId);
		if (customer != null) {
            logger.info("Customer found with ID: {}", customerId);
        } else {
            logger.warn("Customer not found with ID: {}", customerId);
        }
		return  ResponseEntity.status(HttpStatus.OK)
							.contentType(MediaType.APPLICATION_JSON)
							.body(customer);
	}
	
	@GetMapping("/search/name/{customerName}")
	public ResponseEntity<CustomerDTO> findCustomerByName(@PathVariable String customerName){
		logger.info("Received request to find customer by name: {}", customerName);
		CustomerDTO customer=customerService.getCustomer(customerName);
		if (customer != null) {
            logger.info("Customer found with name: {}", customerName);
        } else {
            logger.warn("Customer not found with name: {}", customerName);
        }
		return  ResponseEntity.status(HttpStatus.OK)
				.contentType(MediaType.APPLICATION_JSON)
				.body(customer);
	}
	
	@GetMapping("/viewall")
	public ResponseEntity<List<CustomerDTO>> findAllCustomers(){
		logger.info("Received request to view all customers");
		List<CustomerDTO> customers = customerService.getAllCustomers();
		logger.info("Found {} customers", customers.size());
		return  ResponseEntity.status(HttpStatus.OK)
				.contentType(MediaType.APPLICATION_JSON)
				.body(customers);
	}
	
	@GetMapping("/validate/{customerId}")
	public ResponseEntity<Boolean> validateCustomer(@PathVariable Integer customerId){
		logger.info("Received request from other service to validate customer with ID: {}", customerId);
		return ResponseEntity.status(HttpStatus.OK)
				.body(customerService.isValidCustomer(customerId));
	}
	
	@PostMapping("/login")
	public ResponseEntity<CustomerDTO> login(@RequestBody LoginDto loginDto) {
	    logger.info("Received login request for user: {}", loginDto.getUserName());
	    try {
	        CustomerDTO customerDTO = customerService.login(loginDto);
	        
	        // Make sure userRole is included
	        logger.info("Login successful for user: {}", loginDto.getUserName());
	        return new ResponseEntity<>(customerDTO, HttpStatus.OK);
	    } catch (Exception e) {
	        logger.error("Login failed for user: {}. Reason: {}", loginDto.getUserName(), e.getMessage());
	        return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
	    }
	}

	}
