package com.praveen.Ecommerce.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

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

import com.furnicycle.Ecommerce.constants.AppConstants;
import com.furnicycle.Ecommerce.dto.CategoryDTO;
import com.furnicycle.Ecommerce.entity.CategoryEntity;
import com.furnicycle.Ecommerce.exception.CategoryAlreadyExistsException;
import com.furnicycle.Ecommerce.exception.CategoryNotFoundException;
import com.furnicycle.Ecommerce.mapper.CategoryMapper;
import com.furnicycle.Ecommerce.repository.CategoryRepository;
import com.furnicycle.Ecommerce.service.CategoryServiceImpl;


@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {
	
	@Mock
	private CategoryRepository categoryRepository;
	
	@Mock
	private CategoryMapper categoryMapper;
	
	
	@InjectMocks
	private CategoryServiceImpl categoryService;
	
	private CategoryEntity categoryEntity;
	
	private CategoryDTO categoryDTO,editCategoryDTO;
	
	@BeforeEach
	public void setUp() {
		categoryEntity=new CategoryEntity(1,"Furniture","Wooden Furniture",null);
		categoryDTO=new CategoryDTO(1,"Furniture","Wooden Furniture",null);
		editCategoryDTO=new CategoryDTO(1,"Furniture","Leather",null);
	}
	
	@AfterEach
	public void tearDown() {
		categoryDTO=null;
		categoryEntity=null;
	}
	
	@Test
	public void addCategory_Success_Test() {
		when(categoryRepository.findByCategoryName("Furniture"))
			.thenReturn(Optional.empty());
		when(categoryMapper.toEntity(categoryDTO))
			.thenReturn(categoryEntity);
		when(categoryRepository.save(categoryEntity))
			.thenReturn(categoryEntity);
		when(categoryMapper.toDTO(categoryEntity))
			.thenReturn(categoryDTO);
		
		CategoryDTO result=categoryService.addCategory(categoryDTO);
		assertThat(result).isEqualTo(categoryDTO);
		verify(categoryRepository).save(categoryEntity);
	}
	
	@Test
	public void addCategory_Failed_Test() {
		when(categoryRepository.findByCategoryName("Furniture"))
		.thenReturn(Optional.of(categoryEntity));
		assertThatThrownBy(()->categoryService.addCategory(categoryDTO))
			.isInstanceOf(CategoryAlreadyExistsException.class)
			.hasMessage(AppConstants.CATEGORY__ALREADY_EXISTS_NAME+"Furniture");
		
		verify(categoryRepository,never()).save(categoryEntity);
		
	}
	
	@Test
	public void edit_Category_Success_Test() {
		when(categoryRepository.findById(editCategoryDTO.getCategoryId()))
			.thenReturn(Optional.of(categoryEntity));
		when(categoryRepository.save(categoryEntity))
			.thenReturn(categoryEntity);
		when(categoryMapper.toDTO(categoryEntity))
			.thenReturn(editCategoryDTO);
		
		CategoryDTO update=categoryService.editCategory(categoryDTO);
		
		assertNotNull(update);
		assertEquals(editCategoryDTO.getCategoryId(),update.getCategoryId() );
		assertEquals(editCategoryDTO.getCategoryName(),update.getCategoryName());
		assertEquals(editCategoryDTO.getCategoryDescription(), update.getCategoryDescription());
		
		verify(categoryRepository,times(1)).save(categoryEntity);
		
	}
	
	@Test
	public void edit_Category_Failed_Test() {
		when(categoryRepository.findById(editCategoryDTO.getCategoryId()))
			.thenReturn(Optional.empty());
		assertThatThrownBy(()->categoryService.editCategory(editCategoryDTO))
			.isInstanceOf(CategoryNotFoundException.class)
			.hasMessage(AppConstants.CATEGORY_NOT_FOUND_ID+editCategoryDTO.getCategoryId());
		
		verify(categoryRepository,never()).save(any(CategoryEntity.class));
		
	}
	
	@Test
	public void deleteCategory_Success_Test() {
		when(categoryRepository.findById(1))
			.thenReturn(Optional.of(categoryEntity));
		
		String res=categoryService.deleteCategory(1);
		assertEquals(res, AppConstants.CATEGORY_DELETED);
		verify(categoryRepository, times(1)).deleteById(1);
		
	}
	
	@Test
	public void delete_Category_Failed_Test() {
		when(categoryRepository.findById(33))
			.thenReturn(Optional.empty());
		assertThrows(CategoryNotFoundException.class, ()->{
			categoryService.deleteCategory(33);
		});
		verify(categoryRepository,never()).deleteById(33);
		
	}
	
	@Test
	public void getCategoryById_Success_Test() {
		when(categoryRepository.findById(1))
			.thenReturn(Optional.of(categoryEntity));
		when(categoryMapper.toDTO(categoryEntity))
			.thenReturn(categoryDTO);
		
		CategoryDTO result=categoryService.getCategoryById(1);
		assertNotNull(result);
		assertEquals(result, categoryDTO);
		
		verify(categoryRepository,times(1)).findById(1);
	}
	
	@Test
	public void getCategoryById_Failed_Test() {
		when(categoryRepository.findById(2))
			.thenReturn(Optional.empty());
		
		assertThatThrownBy(()->categoryService.getCategoryById(2))
			.isInstanceOf(CategoryNotFoundException.class)
			.hasMessage(AppConstants.CATEGORY_NOT_FOUND_ID+2);
		
		verify(categoryRepository).findById(2);
	}
	
	@Test
	public void getCategoryByName_Success_Test() {
		when(categoryRepository.findByCategoryName("Furniture"))
		.thenReturn(Optional.of(categoryEntity));
		when(categoryMapper.toDTO(categoryEntity))
		.thenReturn(categoryDTO);
	
		CategoryDTO result=categoryService.getCategoryByName("Furniture");
		assertNotNull(result);
		assertEquals(result, categoryDTO);
		
		verify(categoryRepository,times(1)).findByCategoryName("Furniture");
	}
	
	@Test
	public void getCategoryByName_Failed_Test() {
		when(categoryRepository.findByCategoryName("Test"))
			.thenReturn(Optional.empty());
		
		assertThatThrownBy(()->categoryService.getCategoryByName("Test"))
			.isInstanceOf(CategoryNotFoundException.class)
			.hasMessage(AppConstants.CATEGORY_NOT_FOUND_NAME+"Test");
		
		verify(categoryRepository).findByCategoryName("Test");
	}
	
	
	@Test
	public void getAllCategories_Test() {
		List<CategoryEntity> entities=new ArrayList<>();
		entities.add(categoryEntity);
		
		List<CategoryDTO> dtos=new ArrayList<>();
		dtos.add(categoryDTO);
		
		when(categoryRepository.findAll())
			.thenReturn(entities);
		
		when(categoryMapper.toDTO(categoryEntity))
			.thenReturn(categoryDTO);
		
		List<CategoryDTO> result=categoryService.viewAllCategories();
		
		assertNotNull(result);
		assertEquals(1, result.size());
		assertEquals(categoryDTO.getCategoryName(),dtos.get(0).getCategoryName());
		verify(categoryRepository,times(1)).findAll();
	}
	

}
