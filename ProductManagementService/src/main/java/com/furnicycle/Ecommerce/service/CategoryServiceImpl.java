package com.furnicycle.Ecommerce.service;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.furnicycle.Ecommerce.constants.AppConstants;
import com.furnicycle.Ecommerce.dto.CategoryDTO;
import com.furnicycle.Ecommerce.entity.CategoryEntity;
import com.furnicycle.Ecommerce.exception.CategoryAlreadyExistsException;
import com.furnicycle.Ecommerce.exception.CategoryNotFoundException;
import com.furnicycle.Ecommerce.mapper.CategoryMapper;
import com.furnicycle.Ecommerce.repository.CategoryRepository;

@Service
public class CategoryServiceImpl implements CategoryService {
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private CategoryMapper categoryMapper;
	
	private static final Logger logger = LogManager.getLogger(CategoryServiceImpl.class);

	@Override
	public CategoryDTO addCategory(CategoryDTO categoryDTO) {
		logger.info("Received request to add category: {}", categoryDTO);
		Optional<CategoryEntity> op=categoryRepository.findByCategoryName(categoryDTO.getCategoryName());
		if(op.isPresent()) {
			logger.error("Category already exists with name: {}", categoryDTO.getCategoryName());
			throw new CategoryAlreadyExistsException(AppConstants.CATEGORY__ALREADY_EXISTS_NAME
					+categoryDTO.getCategoryName());
		}
		else {
			CategoryEntity entity=categoryMapper.toEntity(categoryDTO);
			logger.info("Adding category with ID: {}", entity.getCategoryId());
			entity=categoryRepository.save(entity);
			logger.info("Successfully added category with ID: {}", entity.getCategoryId());
			return categoryMapper.toDTO(entity);
		}
	}
	

	@Override
	public CategoryDTO editCategory(CategoryDTO categoryDTO) {
		logger.info("Received request to edit category: {}", categoryDTO);
		Optional<CategoryEntity> op=categoryRepository.findById(categoryDTO.getCategoryId());
		if(op.isEmpty()) {
			logger.warn("Category not found with ID: {}", categoryDTO.getCategoryId());
			throw new CategoryNotFoundException(AppConstants.CATEGORY_NOT_FOUND_ID
					+categoryDTO.getCategoryId());
		}
		else {
			CategoryEntity entity=op.get();
			entity.setCategoryName(categoryDTO.getCategoryName());
	        entity.setCategoryDescription(categoryDTO.getCategoryDescription());
	        logger.info("Editingcategory with ID: {}", entity.getCategoryId());
			entity=categoryRepository.save(entity);
			logger.info("Successfully edited category with ID: {}", entity.getCategoryId());
			return categoryMapper.toDTO(entity);
		}
	}

	@Override
	public String deleteCategory(Integer categoryId) {
		logger.info("Received request to delete category with ID: {}", categoryId);
		Optional<CategoryEntity> op=categoryRepository.findById(categoryId);
		if(op.isEmpty()) {
			logger.error("Category not found with ID: {}", categoryId);
			throw new CategoryNotFoundException(AppConstants.CATEGORY_NOT_FOUND_ID+categoryId);
		}
		else {
			logger.info("Deleting category with ID: {}", categoryId);
			categoryRepository.deleteById(categoryId);
			logger.info("Successfully deleted category with ID: {}", categoryId);
			return AppConstants.CATEGORY_DELETED;
		}
	}

	@Override
	public CategoryDTO getCategoryById(Integer categoryId) {
		logger.info("Received request to get category by ID: {}", categoryId);
		Optional<CategoryEntity> op=categoryRepository.findById(categoryId);
		if(op.isEmpty()) {
			logger.error("Category not found with ID: {}", categoryId);
			throw new CategoryNotFoundException(AppConstants.CATEGORY_NOT_FOUND_ID+categoryId);
		}
		else {
			logger.info("Retrieving category with ID: {}", categoryId);
			CategoryEntity entity=op.get();
			logger.info("Successfully retrieved category with ID: {}", categoryId);
			return categoryMapper.toDTO(entity);			
		}
	}

	@Override
	public CategoryDTO getCategoryByName(String categoryName) {
		logger.info("Received request to get category by name: {}", categoryName);
		Optional<CategoryEntity> op=categoryRepository.findByCategoryName(categoryName);
		if(op.isEmpty()) {
			logger.error("Category not found with name: {}", categoryName);
			throw new CategoryNotFoundException(AppConstants.CATEGORY_NOT_FOUND_NAME+categoryName);
		}
		else {
			logger.info("Retrieving category with name: {}", categoryName);
			CategoryEntity entity=op.get();
			logger.info("Successfully retrieved category with name: {}", categoryName);
			return categoryMapper.toDTO(entity);			
		}
	}

	@Override
	public List<CategoryDTO> viewAllCategories() {
		logger.info("Received request to get all categories");
		List<CategoryEntity> categories=categoryRepository.findAll();
		logger.info("Successfully retrieved {} categories", categories.size());
		return categories.stream().map(categoryMapper::toDTO).toList();
	}

}
