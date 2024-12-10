package com.furnicycle.Ecommerce.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.furnicycle.Ecommerce.Entity.CustomerEntity;
import com.furnicycle.Ecommerce.Entity.UserEntity;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerEntity, Integer>{
	Optional<CustomerEntity> findByCustomerName(String customerName);
	Optional<CustomerEntity> findByUserEntity(UserEntity userEntity);

}
