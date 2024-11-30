package com.furnicycle.Ecommerce.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.furnicycle.Ecommerce.Entity.UserEntity;
import com.furnicycle.Ecommerce.Repository.UserRepository;

@DataJpaTest
public class UserRepositoryTest {
	
	@Autowired
	private UserRepository userRepository;
	
	private UserEntity userEntity;
	
	@BeforeEach
	public void setup() {
		userEntity=new UserEntity(1,"swagat","Swag0810@","ADMIN");
		userRepository.save(userEntity);
	}
	
	@AfterEach
	public void teardown() {
		userEntity=null;
		userRepository.deleteAll();
	}
	
	@Test
	public void findByUsernameTest_Found() {
		Optional<UserEntity> op=userRepository.findByUsername("swagat");
		assertThat(op.get().getUsername()).isEqualTo(userEntity.getUsername());
		assertThat(op.get().getPassword()).isEqualTo(userEntity.getPassword());
	}
	
	@Test
	public void findByUsernameTest_NotFound() {
		Optional<UserEntity> op=userRepository.findByUsername("praveen");
		assertThat(op.isEmpty()).isTrue();
	}
}
