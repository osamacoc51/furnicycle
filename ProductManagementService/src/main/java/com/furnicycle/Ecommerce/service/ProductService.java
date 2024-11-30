package com.furnicycle.Ecommerce.service;

import java.util.List;

import com.furnicycle.Ecommerce.dto.ProductDTO;

public interface ProductService {
	
	public ProductDTO addProduct(ProductDTO productDTO,String categoryName);
	
	public ProductDTO editProduct(ProductDTO productDTO);
	
	public String deleteProduct(Integer productId);
	
	public ProductDTO getProductById(Integer productId);
	
	public ProductDTO getProductByName(String productName);
	
	public List<ProductDTO> viewAllProducts();
	
	public List<ProductDTO> filterProductByCategory(String categoryName);
	
	public ProductDTO updateStockForProduct(Integer productId, Integer newStock);
	
	public boolean isProductValid(Integer productId);
	
}
