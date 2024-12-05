package com.furnicycle.Ecommerce.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.furnicycle.Ecommerce.dto.CategoryDTO;
import com.furnicycle.Ecommerce.service.CategoryService;

@CrossOrigin("*")
@RestController
@RequestMapping("/category")
public class CategoryController {
	
	@Autowired
	private CategoryService categoryService;
	
	private static final Logger logger = LogManager.getLogger(CategoryController.class);
	
	@PostMapping("/add")
	public ResponseEntity<CategoryDTO> addCategory(@RequestBody CategoryDTO categoryDTO){
		logger.info("Received request to add category: {}", categoryDTO);		
		CategoryDTO category=categoryService.addCategory(categoryDTO);
		
		logger.info("Successfully added category with ID: {}", category.getCategoryId());
		return new ResponseEntity<>(category,HttpStatus.CREATED);
	}
	
	
	@PutMapping("/edit")
	public ResponseEntity<CategoryDTO> editCategory(@RequestBody CategoryDTO categoryDTO){
		logger.info("Received request to edit category: {}", categoryDTO);
		CategoryDTO category= categoryService.editCategory(categoryDTO);
		
		logger.info("Successfully edited category with ID: {}", category.getCategoryId());
		return new ResponseEntity<>(category,HttpStatus.OK);
	}
	
	@DeleteMapping("/delete/{categoryId}")
	public ResponseEntity<String> deleteCategory(@PathVariable Integer categoryId){
		logger.info("Received request to delete category with ID: {}", categoryId);
		String result=categoryService.deleteCategory(categoryId);
		
		logger.info("Successfully deleted category with ID: {}", categoryId);
		return new ResponseEntity<>(result,HttpStatus.OK);
	}
	
	@GetMapping("/id/{categoryId}")
	public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable Integer categoryId){
		logger.info("Received request to get category with ID: {}", categoryId);
		CategoryDTO categoryDTO=categoryService.getCategoryById(categoryId);
		
		logger.info("Successfully retrieved category with ID: {}", categoryId);
		return new ResponseEntity<>(categoryDTO,HttpStatus.OK);
	}
	
	@GetMapping("/name/{categoryName}")
	public ResponseEntity<CategoryDTO> getCategoryByName(@PathVariable String categoryName){
		logger.info("Received request to get category with name: {}", categoryName);
		CategoryDTO categoryDTO=categoryService.getCategoryByName(categoryName);
		
		logger.info("Successfully retrieved category with name: {}", categoryName);
		return new ResponseEntity<>(categoryDTO,HttpStatus.OK);
	}
	
	@GetMapping("/viewall")
	public ResponseEntity<List<CategoryDTO>> getAllCategories(){
		logger.info("Received request to get all categories");
		List<CategoryDTO> list=categoryService.viewAllCategories();
		logger.info("Successfully retrieved {} categories", list.size());
		return new ResponseEntity<>(list,HttpStatus.OK);
	}
}
