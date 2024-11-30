package com.praveen.Ecommerce.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doNothing;
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
import com.furnicycle.Ecommerce.dto.ProductDTO;
import com.furnicycle.Ecommerce.entity.CategoryEntity;
import com.furnicycle.Ecommerce.entity.ProductEntity;
import com.furnicycle.Ecommerce.exception.CategoryNotFoundException;
import com.furnicycle.Ecommerce.exception.ProductAlreadyExistsException;
import com.furnicycle.Ecommerce.exception.ProductNotFoundException;
import com.furnicycle.Ecommerce.feign.InventoryClient;
import com.furnicycle.Ecommerce.mapper.CategoryMapper;
import com.furnicycle.Ecommerce.mapper.ProductMapper;
import com.furnicycle.Ecommerce.repository.CategoryRepository;
import com.furnicycle.Ecommerce.repository.ProductRepository;
import com.furnicycle.Ecommerce.service.ProductServiceImpl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
	
	@Mock
	private CategoryRepository categoryRepository;
	
	@Mock
	private CategoryMapper categoryMapper;
	
	@Mock
	private ProductMapper productMapper;
	
	@Mock
	private ProductRepository productRepository;
	
	@Mock
	private InventoryClient inventoryClient;
	
	@InjectMocks
	private ProductServiceImpl productService;
	
	private CategoryEntity categoryEntity;
	private CategoryDTO categoryDTO;
	private ProductEntity productEntity;
	private ProductDTO productDTO,editProduct;
	
	@BeforeEach
	public void setUp() {
		categoryEntity=new CategoryEntity(1,"Furniture","Wooden Furniture",null);
		categoryDTO=new CategoryDTO(1,"Furniture","Wooden Furniture",null);
		categoryEntity.setProductsEntity(new ArrayList<>());
		
		productEntity=new ProductEntity(1,"Table"," 4 seater table",12000.0,1,null);
		productDTO=new ProductDTO(1,"Table"," 4 seater table",12000.0,1,null);
		editProduct=new ProductDTO(1,"Table", "6 seater table",15000.0,1,null);
	}
	
	@AfterEach
	public void tearDown() {
		categoryDTO=null;
		categoryEntity=null;
		productDTO=null;
		productEntity=null;
	}
	
	@Test
	public void add_Product_Failed_ProductAlreadyExists_Test() {
		when(productRepository.findByProductName(productDTO.getProductName()))
			.thenReturn(Optional.of(productEntity));
		
		assertThatThrownBy(()->productService.addProduct(productDTO
				, categoryDTO.getCategoryName()))
				.isInstanceOf(ProductAlreadyExistsException.class)
				.hasMessage(AppConstants.PRODUCT__ALREADY_EXISTS_NAME+productDTO.getProductName());
		
		verify(productRepository,never()).save(productEntity);
	}
	
	@Test
	public void add_Product_Failed_CategoryNotFound_Test() {
		when(productRepository.findByProductName(productDTO.getProductName()))
		.thenReturn(Optional.empty());
		
		when(categoryRepository.findByCategoryName(categoryDTO.getCategoryName()))
		.thenReturn(Optional.empty());
		
		assertThatThrownBy(()->productService.addProduct(productDTO, categoryDTO.getCategoryName()))
			.isInstanceOf(CategoryNotFoundException.class)
			.hasMessage(AppConstants.CATEGORY_NOT_FOUND_NAME+categoryDTO.getCategoryName());
		verify(productRepository,never()).save(productEntity);
	}
	
	@Test
	public void add_Product_Success_Test() {
		when(productRepository.findByProductName(productDTO.getProductName()))
		.thenReturn(Optional.empty());
		
		when(categoryRepository.findByCategoryName(categoryDTO.getCategoryName()))
		.thenReturn(Optional.of(categoryEntity));
		
		when(productMapper.toEntity(productDTO))
		.thenReturn(productEntity);
		
		when(productRepository.save(productEntity))
		.thenReturn(productEntity);
		
		doNothing().when(inventoryClient).addStock(productEntity.getProductId(), productEntity.getStock());
		
	
		
		when(productMapper.toDTO(productEntity))
		.thenReturn(productDTO);
		
		ProductDTO result = productService.addProduct(productDTO, categoryDTO.getCategoryName());
 
        // Assert
        assertNotNull(result);
        assertEquals(productDTO.getProductId(), result.getProductId());
        assertEquals(productDTO.getProductName(), result.getProductName());
        assertEquals(productDTO.getProductDescription(), result.getProductDescription());
        assertEquals(productDTO.getProductPrice(), result.getProductPrice());
 
 
       verify(productRepository, times(1)).save(productEntity);
		
	}
	
	@Test
	public void getProductById_NotFound_Test() {
		when(productRepository.findById(2))
			.thenReturn(Optional.empty());
		
		assertThatThrownBy(()->productService.getProductById(2))
			.isInstanceOf(ProductNotFoundException.class)
			.hasMessage(AppConstants.PRODUCT_NOT_FOUND_ID+2);
		
		verify(productRepository).findById(2);
	}
	
	
	@Test
	public void getProductById_Found_Test() {
		when(productRepository.findById(1))
		.thenReturn(Optional.of(productEntity));
		
		when(productMapper.toDTO(productEntity))
		.thenReturn(productDTO);
		
		ProductDTO result=productService.getProductById(1);
		
		assertNotNull(result);
		assertEquals(result,productDTO);
		
		verify(productRepository, times(1)).findById(1);
	}
	
	@Test
	public void getProductByName_NotFound_Test() {
		when(productRepository.findByProductName(productDTO.getProductName()))
			.thenReturn(Optional.empty());
		
		assertThatThrownBy(()->productService.getProductByName(productDTO.getProductName()))
			.isInstanceOf(ProductNotFoundException.class)
			.hasMessage(AppConstants.PRODUCT_NOT_FOUND_NAME+productDTO.getProductName());
		
		verify(productRepository).findByProductName(productDTO.getProductName());
	}
	
	@Test
	public void getProductByName_Found_Test() {
		when(productRepository.findByProductName(productDTO.getProductName()))
		.thenReturn(Optional.of(productEntity));
		
		when(productMapper.toDTO(productEntity))
		.thenReturn(productDTO);
		
		ProductDTO result=productService.getProductByName(productDTO.getProductName());
		
		assertNotNull(result);
		assertEquals(result,productDTO);
		
		verify(productRepository, times(1)).findByProductName(productDTO.getProductName());
	}
	
	@Test
	public void viewAllProducts_Test() {
		List<ProductEntity> entities=new ArrayList<>();
		entities.add(productEntity);
		
		List<ProductDTO> dtos=new ArrayList<>();
		dtos.add(productDTO);
		
		when(productRepository.findAll()).thenReturn(entities);
		
		when(productMapper.toDTO(productEntity)).thenReturn(productDTO);
		
		List<ProductDTO> result=productService.viewAllProducts();
		
		assertNotNull(result);
		assertEquals(1, result.size());
		assertEquals(productDTO, result.get(0));
		
		verify(productRepository,times(1)).findAll();
	}
	
	@Test
	public void FilterProductsByCategory_Failed_Test() {
		when(categoryRepository.findByCategoryName(categoryDTO.getCategoryName()))
		.thenReturn(Optional.empty());
		
		assertThatThrownBy(()->productService.filterProductByCategory(categoryDTO.getCategoryName()))
			.isInstanceOf(CategoryNotFoundException.class)
			.hasMessage(AppConstants.CATEGORY_NOT_FOUND_NAME+categoryDTO.getCategoryName());
		
		verify(productRepository,never()).findByCategoryEntityCategoryName(categoryDTO.getCategoryName());
		
	}
	
	@Test
	public void FilterProductsByCategory_Success_Test() {
		
		List<ProductEntity> entities=new ArrayList<>();
		entities.add(productEntity);
		
		List<ProductDTO> dtos=new ArrayList<>();
		dtos.add(productDTO);
		
		when(categoryRepository.findByCategoryName(categoryDTO.getCategoryName()))
		.thenReturn(Optional.of(categoryEntity));
		
		when(productRepository.findByCategoryEntityCategoryName(categoryDTO.getCategoryName()))
		.thenReturn(entities);
		
		when(productMapper.toDTO(productEntity)).thenReturn(productDTO);
		
		List<ProductDTO> result=productService.filterProductByCategory(categoryDTO.getCategoryName());
		
		assertNotNull(result);
		assertEquals(1, result.size());
		assertEquals(productDTO, result.get(0));
		
		verify(productRepository,times(1)).findByCategoryEntityCategoryName(categoryDTO.getCategoryName());
		
	}
	
	@Test
	public void deleteProduct_Success_Test() {
		when(productRepository.findById(productDTO.getProductId())).thenReturn(Optional.of(productEntity));
		String result=productService.deleteProduct(productDTO.getProductId());
		
		assertEquals(result, AppConstants.PRODUCT_DELETED);
		verify(productRepository, times(1)).delete(productEntity);
	}
	
	@Test
	public void deleteProduct_Failed_Test() {
		when(productRepository.findById(productDTO.getProductId())).thenReturn(Optional.empty());
		
		assertThatThrownBy(()->productService.deleteProduct(productDTO.getProductId()))
			.isInstanceOf(ProductNotFoundException.class)
			.hasMessage(AppConstants.PRODUCT_NOT_FOUND_ID+productDTO.getProductId());
	}
	
	@Test
	public void editProduct_Failed_Test() {
		when(productRepository.findById(editProduct.getProductId()))
			.thenReturn(Optional.empty());
		assertThatThrownBy(()->productService.editProduct(editProduct))
			.isInstanceOf(ProductNotFoundException.class)
			.hasMessage(AppConstants.PRODUCT_NOT_FOUND_ID+editProduct.getProductId());
		
		verify(productRepository,never()).save(productEntity);
		
	}
	
	@Test
	public void editProduct_Success_Test() {
		when(productRepository.findById(editProduct.getProductId()))
			.thenReturn(Optional.of(productEntity));
		when(productRepository.save(productEntity)).thenReturn(productEntity);
		
		when(productMapper.toDTO(productEntity)).thenReturn(editProduct);
		
		ProductDTO result=productService.editProduct(editProduct);
		
		assertNotNull(result);
		assertEquals(editProduct.getProductId(),result.getProductId() );
		assertEquals(editProduct.getProductName(),result.getProductName());
		assertEquals(editProduct.getProductDescription(), result.getProductDescription());
		assertEquals(editProduct.getProductPrice(),result.getProductPrice());
		
		verify(productRepository,times(1)).save(productEntity);
		
	}
	
	@Test
	public void updateStockForProduct_Success_Test() {
		Integer newStock=5;
		when(productRepository.findById(productDTO.getProductId()))
			.thenReturn(Optional.of(productEntity));
		productDTO.setStock(newStock);
		productEntity.setStock(newStock);
		when(productRepository.save(productEntity)).thenReturn(productEntity);
		when(productMapper.toDTO(productEntity)).thenReturn(productDTO);
		
		ProductDTO result=productService.updateStockForProduct(productDTO.getProductId(), newStock);
		assertNotNull(result);
		assertEquals(productDTO.getProductId(), result.getProductId());
		assertEquals(productDTO.getProductName(),result.getProductName());
		assertEquals(productDTO.getStock(),result.getStock());
		verify(productRepository,times(1)).save(productEntity);
	}
	
	@Test
	public void updateStock_ProductNotFound_Test() {
		Integer newStock=5;
		when(productRepository.findById(productDTO.getProductId()))
			.thenReturn(Optional.empty());
		productDTO.setStock(newStock);
		
		assertThatThrownBy(()->productService.updateStockForProduct(productDTO.getProductId(), newStock))
		.isInstanceOf(ProductNotFoundException.class)
		.hasMessage(AppConstants.PRODUCT_NOT_FOUND_ID+productDTO.getProductId());
		verify(productRepository,never()).save(productEntity);
	}
	
	@Test
	public void validProduct_Test() {
		when(productRepository.findById(anyInt())).thenReturn(Optional.of(productEntity));
		
		assertEquals(productService.isProductValid(1), true);
		verify(productRepository,times(1)).findById(1);
	}
	
	@Test
	public void InvalidProduct_Test() {
		when(productRepository.findById(anyInt())).thenReturn(Optional.empty());
		
		assertEquals(productService.isProductValid(999), false);
		verify(productRepository,times(1)).findById(999);
	}
	
}