package com.furnicycle.Ecommerce.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.furnicycle.Ecommerce.dto.ProductDTO;
import com.furnicycle.Ecommerce.service.ProductService;

@RestController
@RequestMapping("/product")
public class ProductController {

	@Autowired
	private ProductService productService;
	
	private static final Logger logger = LogManager.getLogger(ProductController.class);
	
	@PostMapping("/add/{categoryName}")
	public ResponseEntity<ProductDTO> addProduct(@RequestBody ProductDTO productDTO,
												@PathVariable String categoryName){
		logger.info("Received request to add product: {} under category: {}", productDTO, categoryName);
		ProductDTO product=productService.addProduct(productDTO, categoryName);
		
		logger.info("Successfully added product with ID: {}", product.getProductId());
		return new ResponseEntity<>(product,HttpStatus.CREATED);
	}
	
	@PutMapping("/edit")
	public ResponseEntity<ProductDTO> editProduct(@RequestBody ProductDTO productDTO){
		logger.info("Received request to edit product: {}", productDTO);
		ProductDTO product=productService.editProduct(productDTO);
		logger.info("Successfully edited product with ID: {}", product.getProductId());
		return new ResponseEntity<>(product,HttpStatus.OK); 
	}
	
	@DeleteMapping("/delete/{productId}")
	public ResponseEntity<String> deleteProduct(@PathVariable Integer productId){
		logger.info("Received request to delete product with ID: {}", productId);
		String result=productService.deleteProduct(productId);
		logger.info("Successfully deleted product with ID: {}", productId);
		return new ResponseEntity<>(result,HttpStatus.OK);
	}
	
	@GetMapping("/id/{productId}")
	public ResponseEntity<ProductDTO> getProductById(@PathVariable Integer productId){
		logger.info("Received request to get product with ID: {}", productId);
		ProductDTO productDTO=productService.getProductById(productId);
		logger.info("Successfully retrieved product with ID: {}", productId);
		return new ResponseEntity<>(productDTO,HttpStatus.OK);
	}
	
	@GetMapping("/name/{productName}")
	public ResponseEntity<ProductDTO> getProductByName(@PathVariable String productName){
		logger.info("Received request to get product with name: {}", productName);
		ProductDTO productDTO=productService.getProductByName(productName);
		logger.info("Successfully retrieved product with name: {}", productName);
		return new ResponseEntity<>(productDTO,HttpStatus.OK);
	}
	
	@GetMapping("/viewall")
	public ResponseEntity<List<ProductDTO>> viewAllProducts(){
		logger.info("Received request to get all products");
		List<ProductDTO> list=productService.viewAllProducts();
		logger.info("Successfully retrieved {} products", list.size());
		return new ResponseEntity<>(list, HttpStatus.OK);
		
	}
	
	@GetMapping("/filter/{categoryName}")
	public ResponseEntity<List<ProductDTO>> filterProductsByCategoryName(
				@PathVariable String categoryName){
		logger.info("Received request to filter products by category name: {}", categoryName);
		List<ProductDTO> list=productService.filterProductByCategory(categoryName);
		logger.info("Successfully filtered {} products under category: {}", list.size(), categoryName);
		return new ResponseEntity<>(list,HttpStatus.OK);
	}
	
	@PutMapping("/updatestock/{productId}/{newStock}")
	public ResponseEntity<ProductDTO> updateStock(@PathVariable Integer productId,
												@PathVariable Integer newStock){
		logger.info("Received request to update stock for product ID: {} with new stock: {}", productId, newStock);
		ProductDTO updatedProduct = productService.updateStockForProduct(productId, newStock);
		logger.info("Successfully updated stock for product ID: {}", productId);
        return new ResponseEntity<>(updatedProduct,HttpStatus.OK);
	}
	
	@GetMapping("/validate/{productId}")
	public ResponseEntity<Boolean> validateProduct(@PathVariable Integer productId){
		logger.info("Received request to validate product with ID: {}", productId);
		boolean isValid = productService.isProductValid(productId);
	    logger.info("Validation result for product ID {}: {}", productId, isValid);
		return ResponseEntity.status(HttpStatus.OK)
					.body(productService.isProductValid(productId));
	}
	
	
}
