package com.praveen.Ecommerce.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.furnicycle.Ecommerce.entity.CartEntity;
import com.furnicycle.Ecommerce.repository.CartRepository;


@DataJpaTest
public class CartRepositoryTest {

	@Autowired
	private CartRepository cartRepository;
	
	CartEntity cartEntity;
	
	
	@BeforeEach
	public void setUp() {
		cartEntity=new CartEntity();
		cartEntity.setCreationDate(new Date());
		cartEntity.setCustomerId(1);
		cartRepository.save(cartEntity);
	}
	
	@AfterEach
	public void tearDown() {
		cartEntity=null;
		cartRepository.deleteAll();

	}
	
	@Test
	public void findByCustomerId_Test() {
		Optional<CartEntity> op=cartRepository.findByCustomerId(1);
		assertThat(op.get().getCustomerId()).isEqualTo(1);
	}
	
	@Test
	public void findByCustomerId_InvalidCustomer_Test() {
		Optional<CartEntity> op=cartRepository.findByCustomerId(2);
		assertThat(op.isEmpty()).isTrue();
	}
}
