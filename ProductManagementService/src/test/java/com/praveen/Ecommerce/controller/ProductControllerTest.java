package com.praveen.Ecommerce.controller;



import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.furnicycle.Ecommerce.constants.AppConstants;
import com.furnicycle.Ecommerce.controller.ProductController;
import com.furnicycle.Ecommerce.dto.CategoryDTO;
import com.furnicycle.Ecommerce.dto.ProductDTO;
import com.furnicycle.Ecommerce.exception.CategoryNotFoundException;
import com.furnicycle.Ecommerce.exception.ProductAlreadyExistsException;
import com.furnicycle.Ecommerce.exception.ProductNotFoundException;
import com.furnicycle.Ecommerce.service.ProductService;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {
	
	@MockBean
	private ProductService productService;
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	private ProductDTO product1;
	private ProductDTO product2;
	private CategoryDTO category;
	
	private List<ProductDTO> productList;
	
 
	@BeforeEach
	public void setUp() {
		product1=new ProductDTO(1,"Table","4 seater",12000.0,1,null);
		product2=new ProductDTO(2,"Laptop","15 inch laptop",35000.0,1,null);
		productList=new ArrayList<>();
		productList.add(product1);
		productList.add(product2);
		
		category=new CategoryDTO(1,"Furniture","Table, Sofa, Bed",null);
		
	}
	
//	
	@Test
	public void addProduct_Test() throws JsonProcessingException, Exception {
		when(productService.addProduct(any(ProductDTO.class), anyString()))
		.thenReturn(product1);
		mockMvc.perform(post("/product/add/{categoryName}",category.getCategoryName())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(product1)))
				.andDo(print())
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.productId").value(product1.getProductId()))
				.andExpect(jsonPath("$.productName").value(product1.getProductName()));
	}
//	
	@Test
	public void addProduct_CategoryNotFound_Test() throws JsonProcessingException, Exception {
		when(productService.addProduct(any(ProductDTO.class), anyString()))
		.thenThrow(new CategoryNotFoundException(AppConstants.CATEGORY_NOT_FOUND_NAME+category.getCategoryName()));
		
		mockMvc.perform(post("/product/add/{categoryName}",category.getCategoryName())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(product1)))
				.andDo(print())
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.message")
						.value(AppConstants.CATEGORY_NOT_FOUND_NAME
								+category.getCategoryName()));
	}
	
	@Test
	public void addProduct_ProductAlreadyExists_Test() throws JsonProcessingException, Exception {
		when(productService.addProduct(any(ProductDTO.class), anyString()))
		.thenThrow(new ProductAlreadyExistsException(AppConstants.PRODUCT__ALREADY_EXISTS_NAME+product1.getProductName()));
		
		mockMvc.perform(post("/product/add/{categoryName}",category.getCategoryName())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(product1)))
				.andDo(print())
				.andExpect(status().isConflict())
				.andExpect(jsonPath("$.message")
						.value(AppConstants.PRODUCT__ALREADY_EXISTS_NAME
								+product1.getProductName()));
	}
//	
	@Test
	public void editProduct_Test() throws JsonProcessingException, Exception {
		when(productService.editProduct(any(ProductDTO.class)))
			.thenReturn(product1);
		
		mockMvc.perform(put("/product/edit")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(product1)))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.productId").value(product1.getProductId()));
				
		
	}
//	
	@Test
	public void editProduct_CategoryNotFound_Test() throws JsonProcessingException, Exception {
		when(productService.editProduct(any(ProductDTO.class)))
			.thenThrow(new ProductNotFoundException(AppConstants.PRODUCT_NOT_FOUND_ID+
					product1.getProductId()));
		
		mockMvc.perform(put("/product/edit")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(product1)))
				.andDo(print())
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.message").value(AppConstants.PRODUCT_NOT_FOUND_ID+product1.getProductId()));
				
		
	}
//	
	@Test
	public void deleteProduct_Test() throws JsonProcessingException, Exception {
		when(productService.deleteProduct(anyInt()))
			.thenReturn(AppConstants.PRODUCT_DELETED);
		
		mockMvc.perform(delete("/product/delete/{productId}",product1.getProductId()))
		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(content().string(AppConstants.PRODUCT_DELETED));
				
		
	}
//	
	@Test
	public void deleteProduct_ProductNotFound_Test() throws JsonProcessingException, Exception {
		when(productService.deleteProduct(anyInt()))
		.thenThrow(new ProductNotFoundException(AppConstants.PRODUCT_NOT_FOUND_ID+product1.getProductId()));
		
		mockMvc.perform(delete("/product/delete/{product1Id}",product1.getProductId()))
				.andDo(print())
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.message").value(AppConstants.PRODUCT_NOT_FOUND_ID+product1.getProductId()));
				
		
	}
//	
	@Test
	public void getProductById_Test() throws JsonProcessingException, Exception {
		when(productService.getProductById(anyInt()))
			.thenReturn(product1);
				
		
		mockMvc.perform(get("/product/id/{productId}",product1.getProductId()))
		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.productId").value(product1.getProductId()))
		.andExpect(jsonPath("$.productName").value(product1.getProductName()));
			
	}
//	
	@Test
	public void getProductById_CategoryNotFound_Test() throws JsonProcessingException, Exception {
		when(productService.getProductById(anyInt()))
		.thenThrow(new ProductNotFoundException(AppConstants.PRODUCT_NOT_FOUND_ID+product1.getProductId()));
		
		mockMvc.perform(get("/product/id/{productId}",product1.getProductId()))
				.andDo(print())
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.message").value(AppConstants.PRODUCT_NOT_FOUND_ID+product1.getProductId()));
				
		
	}
//	
	@Test
	public void getProductByName_Test() throws JsonProcessingException, Exception {
		when(productService.getProductByName(anyString()))
			.thenReturn(product1);
				
		
		mockMvc.perform(get("/product/name/{productName}",product1.getProductName()))
		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.productId").value(product1.getProductId()))
		.andExpect(jsonPath("$.productName").value(product1.getProductName()));
			
	}
	
	@Test
	public void getProductByName_ProductNotFound_Test() throws JsonProcessingException, Exception {
		when(productService.getProductByName(anyString()))
		.thenThrow(new ProductNotFoundException(AppConstants.PRODUCT_NOT_FOUND_NAME+product1.getProductName()));
		
		mockMvc.perform(get("/product/name/{productName}",product1.getProductName()))
				.andDo(print())
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.message").value(AppConstants.PRODUCT_NOT_FOUND_NAME+product1.getProductName()));
				
		
	}
//	
	@Test
	public void viewAllProductss_Test() throws Exception{
		when(productService.viewAllProducts()).thenReturn(productList);
		mockMvc.perform(get("/product/viewall"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].productId").value(product1.getProductId()))
				.andExpect(jsonPath("$[0].productName").value(product1.getProductName()));
		
	}
	
	@Test
	public void filterProductByCategory_Test() throws Exception {
		when(productService.filterProductByCategory(anyString()))
			.thenReturn(productList);
		mockMvc.perform(get("/product/filter/{categoryName}",category.getCategoryName()))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$[0].productId").value(product1.getProductId()))
			.andExpect(jsonPath("$[0].productName").value(product1.getProductName()));
	
	}
	
	@Test
	public void filterProductByCategory_CategoryNotFound_Test() throws Exception {
		when(productService.filterProductByCategory(anyString()))
		.thenThrow(new CategoryNotFoundException(AppConstants.CATEGORY_NOT_FOUND_NAME+category.getCategoryName()));
		
		mockMvc.perform(get("/product/filter/{categoryName}",category.getCategoryName()))
			.andDo(print())
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.message").value(AppConstants.CATEGORY_NOT_FOUND_NAME+category.getCategoryName()));
			
	
	}
	
	@Test
	public void updateProductStock_Test() throws Exception {
		Integer newStock=6;
		product1.setStock(newStock);
		when(productService.updateStockForProduct(product1.getProductId(), newStock))
		.thenReturn(product1);
		
		mockMvc.perform(put("/product/updatestock/{productId}/{newStock}",product1.getProductId(),newStock))
		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.productName").value(product1.getProductName()))
		.andExpect(jsonPath("$.productId").value(product1.getProductId()))
		.andExpect(jsonPath("$.stock").value(newStock));
	
	}
	
	@Test
	public void updateProductStock_ProductNotFound_Test() throws Exception {
		Integer newStock=6;
		product1.setStock(newStock);
		when(productService.updateStockForProduct(product1.getProductId(), newStock))
		.thenThrow(new ProductNotFoundException(AppConstants.PRODUCT_NOT_FOUND_ID+product1.getProductId()));
		
		mockMvc.perform(put("/product/updatestock/{productId}/{newStock}",product1.getProductId(),newStock))
		.andDo(print())
		.andExpect(status().isNotFound())
		.andExpect(jsonPath("$.message").value(AppConstants.PRODUCT_NOT_FOUND_ID+product1.getProductId()));
	}
}