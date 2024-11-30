package com.furnicycle.Ecommerce.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.furnicycle.Ecommerce.entity.ProductEntity;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Integer>{
	Optional<ProductEntity> findByProductName(String productName);
	
	List<ProductEntity> findByCategoryEntityCategoryName(String categoryName);
}
