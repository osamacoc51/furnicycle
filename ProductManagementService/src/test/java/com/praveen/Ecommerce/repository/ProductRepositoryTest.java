package com.praveen.Ecommerce.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.furnicycle.Ecommerce.entity.CategoryEntity;
import com.furnicycle.Ecommerce.entity.ProductEntity;
import com.furnicycle.Ecommerce.repository.CategoryRepository;
import com.furnicycle.Ecommerce.repository.ProductRepository;

@DataJpaTest
public class ProductRepositoryTest {
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	private ProductEntity productEntity;
	
	private  CategoryEntity categoryEntity;
	
	
	@BeforeEach
	public void setUp() {
		categoryEntity = new CategoryEntity();
	    categoryEntity.setCategoryName("Furniture");
	    categoryEntity.setCategoryDescription("Sofa, Bed");
	    List<ProductEntity> list=new ArrayList<>();
	    categoryEntity.setProductsEntity(list);
	    
	    productEntity = new ProductEntity();
	    productEntity.setProductName("Table");
	    productEntity.setProductDescription("4 seater Table");
	    productEntity.setProductPrice(25000.0);
	    
	    categoryEntity.addPrdouct(productEntity);
	    
	    categoryRepository.save(categoryEntity);
	}
	
	
	@AfterEach
	public void tearDown() {
	    categoryRepository.deleteAll();
	    productRepository.deleteAll();
	}
	
	@Test
	public void findByProductName_Found_Test() {
		Optional<ProductEntity> op=productRepository.findByProductName("Table");
		assertThat(op.get().getProductName()).isEqualTo(productEntity.getProductName());
		
	}
	
	@Test
	public void findByProductName_NotFound_Test() {
		Optional<ProductEntity> op=productRepository.findByProductName("sofa");
		assertThat(op.isEmpty()).isTrue();
	}
	
	@Test
	public void findByCategoryName_Found_Test() {
		List<ProductEntity> op=productRepository.findByCategoryEntityCategoryName("Furniture");
		assertThat(op.get(0).getProductName()).isEqualTo(productEntity.getProductName());
		
	}
	
	@Test
	public void findByCategoryName_NotFound_Test() {
		List<ProductEntity> op=productRepository.findByCategoryEntityCategoryName("Furni");
		assertThat(op.isEmpty()).isTrue();
		
	}
	
}
