package com.praveen.Ecommerce.repository;

import static org.assertj.core.api.Assertions.assertThat;


import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.furnicycle.Ecommerce.entity.CategoryEntity;
import com.furnicycle.Ecommerce.repository.CategoryRepository;

@DataJpaTest
public class CategoryRepositoryTest {

	@Autowired
	private CategoryRepository categoryRepository;
	
	private CategoryEntity categoryEntity;
	
	Integer categoryId;
	String categoryName;
	String categoryDescription;
	@BeforeEach
	public void setUp() {
		categoryId=1;
		categoryName="Furniture";
		categoryDescription="Sofa,Bed,Table";
		
		categoryEntity=new CategoryEntity(categoryId,categoryName,categoryDescription,null);
		categoryRepository.save(categoryEntity);
		
	}
	
	@AfterEach
	public void tearDown() {
		categoryEntity=null;
		categoryRepository.deleteAll();
	}
	
	@Test
	public void findByCategoryName_Found_Test() {
		Optional<CategoryEntity> op=categoryRepository.findByCategoryName(categoryName);
		assertThat(op.get().getCategoryName()).isEqualTo(categoryEntity.getCategoryName());
	}
	
	@Test
	public void findByCategoryName_NotFound_Test() {
		Optional<CategoryEntity> op=categoryRepository.findByCategoryName("ZZZZ");
		assertThat(op.isEmpty()).isTrue();
	}
}
