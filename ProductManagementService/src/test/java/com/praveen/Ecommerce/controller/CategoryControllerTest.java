package com.praveen.Ecommerce.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

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
import com.furnicycle.Ecommerce.controller.CategoryController;
import com.furnicycle.Ecommerce.dto.CategoryDTO;
import com.furnicycle.Ecommerce.exception.CategoryAlreadyExistsException;
import com.furnicycle.Ecommerce.exception.CategoryNotFoundException;
import com.furnicycle.Ecommerce.service.CategoryService;

@WebMvcTest(CategoryController.class)
public class CategoryControllerTest {
	
	@MockBean
	private CategoryService categoryService;
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	private CategoryDTO category1;
	private CategoryDTO category2;
	
	private List<CategoryDTO> categories;
	
	@BeforeEach
	public void setUp() {
		category1=new CategoryDTO(1,"Furniture","Table, Sofa, Bed",null);
		category2=new CategoryDTO(2,"Electronics","Bulb, Tv, headset",null);
		categories=new ArrayList<>();
		categories.add(category1);
		categories.add(category2);
		
	}
	
	@Test
	public void addCategory_Test() throws JsonProcessingException, Exception {
		when(categoryService.addCategory(any(CategoryDTO.class))).thenReturn(category1);
		mockMvc.perform(post("/category/add")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(category1)))
				.andDo(print())
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.categoryId").value(category1.getCategoryId()))
				.andExpect(jsonPath("$.categoryName").value(category1.getCategoryName()));
	}
	
	@Test
	public void addCategory_CategoryAlreadyExists_Test() throws JsonProcessingException, Exception {
		when(categoryService.addCategory(any(CategoryDTO.class)))
			.thenThrow(new CategoryAlreadyExistsException(
					AppConstants.CATEGORY__ALREADY_EXISTS_NAME+category1.getCategoryName()));
		
		mockMvc.perform(post("/category/add")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(category1)))
				.andDo(print())
				.andExpect(status().isConflict())
				.andExpect(jsonPath("$.message")
						.value(AppConstants.CATEGORY__ALREADY_EXISTS_NAME
								+category1.getCategoryName()));
	}
	
	@Test
	public void editCategory_Test() throws JsonProcessingException, Exception {
		when(categoryService.editCategory(any(CategoryDTO.class)))
			.thenReturn(category1);
		
		mockMvc.perform(put("/category/edit")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(category1)))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.categoryId").value(category1.getCategoryId()));
				
		
	}
	
	@Test
	public void editCategory_CategoryNotFound_Test() throws JsonProcessingException, Exception {
		when(categoryService.editCategory(any(CategoryDTO.class)))
			.thenThrow(new CategoryNotFoundException(AppConstants.CATEGORY_NOT_FOUND_ID+
					category1.getCategoryId()));
		
		mockMvc.perform(put("/category/edit")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(category1)))
				.andDo(print())
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.message").value(AppConstants.CATEGORY_NOT_FOUND_ID+category1.getCategoryId()));
				
		
	}
	
	@Test
	public void deleteCategory_Test() throws JsonProcessingException, Exception {
		when(categoryService.deleteCategory(anyInt()))
			.thenReturn(AppConstants.CATEGORY_DELETED);
		
		mockMvc.perform(delete("/category/delete/{categoryId}",category1.getCategoryId()))
		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(content().string(AppConstants.CATEGORY_DELETED));
				
		
	}
	
	@Test
	public void deleteCategory_CategoryNotFound_Test() throws JsonProcessingException, Exception {
		when(categoryService.deleteCategory(anyInt()))
		.thenThrow(new CategoryNotFoundException(AppConstants.CATEGORY_NOT_FOUND_ID+category1.getCategoryId()));
		
		mockMvc.perform(delete("/category/delete/{categoryId}",category1.getCategoryId()))
				.andDo(print())
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.message").value(AppConstants.CATEGORY_NOT_FOUND_ID+category1.getCategoryId()));
				
		
	}
	
	@Test
	public void getCategoryById_Test() throws JsonProcessingException, Exception {
		when(categoryService.getCategoryById(anyInt()))
			.thenReturn(category1);
				
		
		mockMvc.perform(get("/category/id/{categoryId}",category1.getCategoryId()))
		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.categoryId").value(category1.getCategoryId()))
		.andExpect(jsonPath("$.categoryName").value(category1.getCategoryName()));
			
	}
	
	@Test
	public void getCategoryById_CategoryNotFound_Test() throws JsonProcessingException, Exception {
		when(categoryService.getCategoryById(anyInt()))
		.thenThrow(new CategoryNotFoundException(AppConstants.CATEGORY_NOT_FOUND_ID+category1.getCategoryId()));
		
		mockMvc.perform(get("/category/id/{categoryId}",category1.getCategoryId()))
				.andDo(print())
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.message").value(AppConstants.CATEGORY_NOT_FOUND_ID+category1.getCategoryId()));
				
		
	}
	
	@Test
	public void getCategoryByName_Test() throws JsonProcessingException, Exception {
		when(categoryService.getCategoryByName(anyString()))
			.thenReturn(category1);
				
		
		mockMvc.perform(get("/category/name/{categoryName}",category1.getCategoryName()))
		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.categoryId").value(category1.getCategoryId()))
		.andExpect(jsonPath("$.categoryName").value(category1.getCategoryName()));
			
	}
	
	@Test
	public void getCategoryByName_CategoryNotFound_Test() throws JsonProcessingException, Exception {
		when(categoryService.getCategoryByName(anyString()))
		.thenThrow(new CategoryNotFoundException(AppConstants.CATEGORY_NOT_FOUND_NAME+category1.getCategoryName()));
		
		mockMvc.perform(get("/category/name/{categoryName}",category1.getCategoryName()))
				.andDo(print())
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.message").value(AppConstants.CATEGORY_NOT_FOUND_NAME+category1.getCategoryName()));
				
		
	}
	
	@Test
	public void viewAllCategories_Test() throws Exception{
		when(categoryService.viewAllCategories()).thenReturn(categories);
		mockMvc.perform(get("/category/viewall"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].categoryId").value(category1.getCategoryId()))
				.andExpect(jsonPath("$[0].categoryName").value(category1.getCategoryName()));
		
	}
	
	
	
}
