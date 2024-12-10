package com.furnicycle.Ecommerce.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.furnicycle.Ecommerce.DTO.CustomerDTO;
import com.furnicycle.Ecommerce.DTO.LoginDto;
import com.furnicycle.Ecommerce.DTO.UserDTO;
import com.furnicycle.Ecommerce.Entity.CustomerEntity;
import com.furnicycle.Ecommerce.Entity.UserEntity;
import com.furnicycle.Ecommerce.Repository.CustomerRepository;
import com.furnicycle.Ecommerce.Repository.UserRepository;
import com.furnicycle.Ecommerce.exception.CustomerAlreadyExistsException;
import com.furnicycle.Ecommerce.exception.CustomerNotFoundException;
import com.furnicycle.Ecommerce.exception.UserAlreadyExistsException;
import com.furnicycle.Ecommerce.feign.CartServiceClient;
import com.furnicycle.Ecommerce.mapper.CustomerMapper;
import com.furnicycle.Ecommerce.mapper.UserMapper;

import jakarta.transaction.Transactional;

@Service
public class CustomerServiceImpl implements CustomerService {
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CustomerMapper customerMapper;
	
	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private CartServiceClient cartServiceClient;
	
	private static final Logger logger = LogManager.getLogger(CustomerServiceImpl.class);

	@Override
	@Transactional
	public CustomerDTO addCustomer(CustomerDTO customer) {
		logger.info("Attempting to add a new customer: {}", customer.getCustomerName());
		Optional<CustomerEntity> opCust=customerRepository.findByCustomerName(customer.getCustomerName());
		
		if(opCust.isPresent()) {
			logger.warn("Customer already exists with name: {}", customer.getCustomerName());
			throw new CustomerAlreadyExistsException("Customer already exists with name "+customer.getCustomerName());
		}
		
		UserDTO user=customer.getUserDTO();
		Optional<UserEntity> opUser=userRepository.findByUsername(user.getUsername());
		if(opUser.isPresent()) {
			logger.warn("User already exists with username: {}", user.getUsername());
			throw new UserAlreadyExistsException("User already exists with Username : "+opUser.get().getUsername());
		}
		CustomerEntity entity=customerMapper.toEntity(customer);
		logger.info("Saving new user: {}", user.getUsername());
		UserEntity userEntity=userMapper.toEntity(user);
		userRepository.save(userEntity);
		logger.info("Saved new user: {}", user.getUsername());
		entity.setUserEntity(userEntity);
		
		logger.info("Saving new customer: {}", entity.getCustomerName());
		CustomerEntity savedCustomer=customerRepository.save(entity);
		logger.info("Successfully added customer with ID: {}", savedCustomer.getCustomerId());
		
		logger.info("Sending request to cart-service for cart creation of customer :"+savedCustomer);
		String cartResponse=cartServiceClient.createCart(savedCustomer.getCustomerId());
		logger.info(cartResponse);
		return customerMapper.toDTO(savedCustomer);
	}
	
	
	
	@Override
	public CustomerDTO editCustomer(CustomerDTO customer) {
		logger.info("Attempting to edit customer with ID: {}", customer.getCustomerId());
		CustomerEntity entity=customerRepository.save(customerMapper.toEntity(customer));
		logger.info("Successfully edited customer with ID: {}", entity.getCustomerId());
		return customerMapper.toDTO(entity);
	}

	@Override
	public String deleteCustomer(Integer customerId) {
		logger.info("Attempting to delete customer with ID: {}", customerId);
		Optional<CustomerEntity> op=customerRepository.findById(customerId);
		if(op.isEmpty()) {
			logger.warn("No customer found with ID: {}", customerId);
			throw new CustomerNotFoundException("No customer found with Id: "+customerId);
		}
		customerRepository.deleteById(customerId);
		logger.info("Successfully deleted customer with ID: {}", customerId);
		return "customer deleted sucessfully";
	}

	@Override
	public CustomerDTO getCustomer(Integer customerId) {
		logger.info("Searching for customer with ID: {}", customerId);
		Optional<CustomerEntity> op=customerRepository.findById(customerId);
		if(op.isEmpty()) {
			logger.warn("No customer found with ID: {}", customerId);
			throw new CustomerNotFoundException("No customer found with Id: "+customerId);
		}
		CustomerEntity entity=op.get();
		logger.info("Customer found with ID: {}", customerId);
		return customerMapper.toDTO(entity);
		
	}

	@Override
	public CustomerDTO getCustomer(String customerName) {
		logger.info("Searching for customer with name: {}", customerName);
		Optional<CustomerEntity> entity=customerRepository.findByCustomerName(customerName);
		if(entity.isEmpty()) {
			logger.warn("No customer found with name: {}", customerName);
			throw new CustomerNotFoundException("No customer found with name : "+customerName);
		}
		CustomerEntity cust=entity.get();
		logger.info("Customer found with name: {}", customerName);
		return customerMapper.toDTO(cust);
	}

	@Override
	public List<CustomerDTO> getAllCustomers() {
		logger.info("Fetching all customers");
		List<CustomerEntity> customers=customerRepository.findAll();
		logger.info("Found {} customers", customers.size());
		return customers.stream().map(customerMapper::toDTO).collect(Collectors.toList());
	}

	@Override
	public boolean isValidCustomer(Integer customerId) {
		logger.info("Validating customer with ID: {}", customerId);
		Optional<CustomerEntity> op=customerRepository.findById(customerId);
		if(op.isPresent()) {
			logger.info("Customer with ID: {} is valid", customerId);
			return true;
		}
		else {
			logger.warn("Customer with ID: {} is not valid", customerId);
			return false;
		}
	}



	@Override
	public CustomerDTO login(LoginDto loginDto) {
	    logger.info("Attempting login for user: {}", loginDto.getUserName());

	    // Fetch the user by username
	    Optional<UserEntity> optionalUser = userRepository.findByUsername(loginDto.getUserName());
	    if (!optionalUser.isPresent()) {
	        logger.warn("User not found with username: {}", loginDto.getUserName());
	        throw new CustomerNotFoundException("User not found with username: " + loginDto.getUserName());
	    }

	    UserEntity user = optionalUser.get();
	    logger.info("User found with username: {}", loginDto.getUserName());

	    // Validate password
	    if (!user.getPassword().equals(loginDto.getPassword())) {
	        logger.warn("Invalid password for username: {}", loginDto.getUserName());
	        throw new RuntimeException("Invalid password");
	    }
	    logger.info("Password validated for username: {}", loginDto.getUserName());

	    // Fetch the associated customer
	    Optional<CustomerEntity> optionalCustomer = customerRepository.findByUserEntity(user);
	    if (!optionalCustomer.isPresent()) {
	        logger.warn("No customer associated with user: {}", loginDto.getUserName());
	        throw new CustomerNotFoundException("No customer associated with the provided credentials");
	    }

	    CustomerEntity customerEntity = optionalCustomer.get();
	    logger.info("Customer associated with username: {} found with ID: {}", loginDto.getUserName(), customerEntity.getCustomerId());

	    // Map the CustomerEntity to CustomerDTO
	    CustomerDTO customerDTO = new CustomerDTO();
	    customerDTO.setCustomerId(customerEntity.getCustomerId());
	    customerDTO.setCustomerName(customerEntity.getCustomerName());
	    customerDTO.setAddress(customerEntity.getAddress());

	    // Set the userDTO in the CustomerDTO
	    UserDTO userDTO = new UserDTO();
	    userDTO.setUserId(user.getUserId());
	    userDTO.setUsername(user.getUsername());
	    userDTO.setPassword(user.getPassword());
	    userDTO.setUserRole(user.getUserRole());

	    customerDTO.setUserDTO(userDTO);

	    logger.info("Login successful for user: {}. Returning customer details.", loginDto.getUserName());
	    return customerDTO;
	}


}
