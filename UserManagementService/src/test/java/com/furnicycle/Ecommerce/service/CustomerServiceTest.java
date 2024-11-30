package com.furnicycle.Ecommerce.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.furnicycle.Ecommerce.DTO.CustomerDTO;
import com.furnicycle.Ecommerce.DTO.UserDTO;
import com.furnicycle.Ecommerce.Entity.CustomerEntity;
import com.furnicycle.Ecommerce.Entity.UserEntity;
import com.furnicycle.Ecommerce.Repository.CustomerRepository;
import com.furnicycle.Ecommerce.Repository.UserRepository;
import com.furnicycle.Ecommerce.Service.CustomerServiceImpl;
import com.furnicycle.Ecommerce.exception.CustomerAlreadyExistsException;
import com.furnicycle.Ecommerce.exception.CustomerNotFoundException;
import com.furnicycle.Ecommerce.exception.UserAlreadyExistsException;
import com.furnicycle.Ecommerce.feign.CartServiceClient;
import com.furnicycle.Ecommerce.mapper.CustomerMapper;
import com.furnicycle.Ecommerce.mapper.UserMapper;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {
	
	@Mock
	private CustomerRepository customerRepository;
	
	@Mock
	private UserRepository userRepository;
	
	@Mock
	private CustomerMapper customerMapper;
	
	@Mock
	private UserMapper userMapper;
	
	@InjectMocks
	private CustomerServiceImpl customerService;
	
	@Mock
	private CartServiceClient cartServiceClient;
	
	
	private CustomerEntity customerEntity;
	
	private UserEntity userEntity;
	
	private UserDTO userDTO;
	
	private CustomerDTO customerDTO;
	
	
	
	
	
	@BeforeEach
	public void setUp() {
		
		userEntity=new UserEntity(1,"swagat","Swag0810@","ADMIN");
		customerEntity=new CustomerEntity(1,"Swagat Jena","Bengaluru",userEntity);
		userDTO=new UserDTO(1,"swagat","Swag0810@","ADMIN");
		customerDTO=new CustomerDTO(1,"Swagat Jena","Bengaluru",userDTO);
			
			
	}
	
	@AfterEach
	public void tearDown() {
		userEntity=null;
		userDTO=null;
		customerDTO=null;
		customerEntity=null;
	}
	
	@Test
	public void addCustomerTest_Success() {
		when(customerRepository.findByCustomerName(customerDTO.getCustomerName()))
			.thenReturn(Optional.empty());
		when(userRepository.findByUsername(userDTO.getUsername()))
			.thenReturn(Optional.empty());
		when(customerMapper.toEntity(customerDTO)).thenReturn(customerEntity);
		when(userMapper.toEntity(userDTO)).thenReturn(userEntity);
		when(userRepository.save(userEntity)).thenReturn(userEntity);
		when(customerRepository.save(customerEntity)).thenReturn(customerEntity);
		when(cartServiceClient.createCart(anyInt())).thenReturn("Cart Created Successfully for customer: "+1);
		when(customerMapper.toDTO(any(CustomerEntity.class))).thenReturn(customerDTO);
		
		CustomerDTO result=customerService.addCustomer(customerDTO);
		assertThat(result).isEqualTo(customerDTO);
		verify(customerRepository).save(customerEntity);
		verify(userRepository).save(userEntity);
	}
	
	@Test
	public void addCustomerTest_CustomerExists() {
		when(customerRepository.findByCustomerName(customerDTO.getCustomerName()))
		.thenReturn(Optional.of(customerEntity));
		
		assertThatThrownBy(()->customerService.addCustomer(customerDTO))
		.isInstanceOf(CustomerAlreadyExistsException.class)
		.hasMessage("Customer already exists with name " + customerDTO.getCustomerName());
		
		verify(customerRepository, never()).save(any(CustomerEntity.class));
        verify(userRepository, never()).save(any(UserEntity.class));
			
	}
	
	@Test
	public void addCustomerTest_UserExists() {
		when(userRepository.findByUsername(userDTO.getUsername()))
		.thenReturn(Optional.of(userEntity));
		
		assertThatThrownBy(()->customerService.addCustomer(customerDTO))
		.isInstanceOf(UserAlreadyExistsException.class)
		.hasMessage("User already exists with Username : " + userDTO.getUsername());
	}
	
	@Test
	public void editCustomer_Test() {
		when(customerMapper.toEntity(customerDTO)).thenReturn(customerEntity);
		when(customerRepository.save(customerEntity)).thenReturn(customerEntity);
		when(customerMapper.toDTO(customerEntity)).thenReturn(customerDTO);
		
		
		CustomerDTO result=customerService.editCustomer(customerDTO);
		assertThat(result).isEqualTo(customerDTO);
		verify(customerRepository).save(any(CustomerEntity.class));
	}
	
	@Test
	public void deleteCustomer_Test() {
		when(customerRepository.findById(anyInt()))
			.thenReturn(Optional.of(customerEntity));
		
		String res=customerService.deleteCustomer(anyInt());
		assertThat(res).isEqualTo("customer deleted sucessfully");
		verify(customerRepository).deleteById(anyInt());
	}
	
	@Test
	public void deleteCustomer_NotFoundTest() {
		when(customerRepository.findById(anyInt()))
			.thenReturn(Optional.empty());
		
		assertThrows(CustomerNotFoundException.class, ()->{
			customerService.deleteCustomer(anyInt());
		});
	}
	
	@Test
	public void getCustomerById_Test() {
		when(customerRepository.findById(anyInt()))
		.thenReturn(Optional.of(customerEntity));
		
		when(customerMapper.toDTO(customerEntity))
		.thenReturn(customerDTO);
		
		CustomerDTO cust=customerService.getCustomer(anyInt());
		
		assertNotNull(cust);
		assertThat(cust).isEqualTo(customerDTO);
		verify(customerRepository).findById(anyInt());
	}
	
	@Test
	public void getCustomerByID_NotFoundTest() {
		when(customerRepository.findById(anyInt()))
		.thenReturn(Optional.empty());
		
		assertThrows(CustomerNotFoundException.class, ()->{
			customerService.getCustomer(anyInt());
		});		
	}
	
	@Test
	public void getCustomerByName_Test() {
		when(customerRepository.findByCustomerName(anyString()))
		.thenReturn(Optional.of(customerEntity));
		
		when(customerMapper.toDTO(customerEntity))
		.thenReturn(customerDTO);
		
		CustomerDTO cust=customerService.getCustomer(anyString());
		
		assertNotNull(cust);
		assertThat(cust).isEqualTo(customerDTO);
		verify(customerRepository).findByCustomerName(anyString());
	}
	
	@Test
	public void getCustomerByName_NotFoundTest() {
		when(customerRepository.findByCustomerName(anyString()))
		.thenReturn(Optional.empty());
		
		assertThrows(CustomerNotFoundException.class, ()->{
			customerService.getCustomer(anyString());
		});		
	}
	
	@Test
	public void getAllCustomers_Test() {
		  List<CustomerEntity> customerEntities = new ArrayList<>();
	        customerEntities.add(customerEntity);
	        
	      List<CustomerDTO> customerDTOs = new ArrayList<>();
	        customerDTOs.add(customerDTO);
	      
	      when(customerRepository.findAll())
	      	.thenReturn(customerEntities);
	      when(customerMapper.toDTO(customerEntity))
	      	.thenReturn(customerDTO);
	      
	      List<CustomerDTO> result=customerService.getAllCustomers();
	      
	      assertNotNull(result);
	      assertThat(1).isEqualTo(result.size());
	      assertThat(customerDTO.getCustomerName()).isEqualTo(result.get(0).getCustomerName());
	}
	
	@Test
	public void validCustomer_Test() {
		when(customerRepository.findById(anyInt())).thenReturn(Optional.of(customerEntity));
		assertEquals(customerService.isValidCustomer(1), true);
		verify(customerRepository,times(1)).findById(1);
	}
	
	@Test
	public void NotvalidCustomer_Test() {
		when(customerRepository.findById(anyInt())).thenReturn(Optional.empty());
		assertEquals(customerService.isValidCustomer(1), false);
		verify(customerRepository,times(1)).findById(1);
	}
}