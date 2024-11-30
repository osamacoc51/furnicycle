package com.furnicycle.Ecommerce.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.furnicycle.Ecommerce.Entity.CustomerEntity;
import com.furnicycle.Ecommerce.Entity.UserEntity;
import com.furnicycle.Ecommerce.Repository.CustomerRepository;

@DataJpaTest
public class CustomerRepositoryTest {
	
	@Autowired
	private CustomerRepository customerRepository;
	
	CustomerEntity customerEntity;
	
	UserEntity userEntity;
	
	@BeforeEach
	public void setUp() {
		//userEntity=new UserEntity(1,"Swagat","Swag08@","ADMIN");
		userEntity=new UserEntity();
		customerEntity=new CustomerEntity(1,"Swagat Jena","Munnekolala, Bangalore",userEntity);
		customerRepository.save(customerEntity);
		
	}
	
	@AfterEach
	public void tearDown() {
		//userEntity=null;
		customerEntity=null;
		customerRepository.deleteAll();
	}
	
	//Test case success
	@Test
	public void findByCustomerName_Found() {
		Optional<CustomerEntity> op=customerRepository.findByCustomerName("Swagat Jena");
		assertThat(op.get().getCustomerName()).isEqualTo(customerEntity.getCustomerName());
		
	}
	
	
	//Test Case Failure
	@Test
	public void findByCustomerName_NotFound() {
		Optional<CustomerEntity> op=customerRepository.findByCustomerName("Praveen");
		assertThat(op.isEmpty()).isTrue();
	}

}
