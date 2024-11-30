package com.furnicycle.Ecommerce.service;

import java.util.List;

import com.furnicycle.Ecommerce.dto.CategoryDTO;

public interface CategoryService {
	public CategoryDTO addCategory(CategoryDTO categoryDTO);
	
	public CategoryDTO editCategory(CategoryDTO categoryDTO);
	
	public String deleteCategory(Integer categoryId);
	
	public CategoryDTO getCategoryById(Integer categoryId);
	
	public CategoryDTO getCategoryByName(String categoryName);
	
	public List<CategoryDTO> viewAllCategories();
}
