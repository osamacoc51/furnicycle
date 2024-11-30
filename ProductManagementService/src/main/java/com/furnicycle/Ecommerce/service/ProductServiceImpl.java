package com.furnicycle.Ecommerce.service;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.furnicycle.Ecommerce.constants.AppConstants;
import com.furnicycle.Ecommerce.dto.ProductDTO;
import com.furnicycle.Ecommerce.entity.CategoryEntity;
import com.furnicycle.Ecommerce.entity.ProductEntity;
import com.furnicycle.Ecommerce.exception.CategoryNotFoundException;
import com.furnicycle.Ecommerce.exception.ProductAlreadyExistsException;
import com.furnicycle.Ecommerce.exception.ProductNotFoundException;
import com.furnicycle.Ecommerce.feign.InventoryClient;
import com.furnicycle.Ecommerce.mapper.ProductMapper;
import com.furnicycle.Ecommerce.repository.CategoryRepository;
import com.furnicycle.Ecommerce.repository.ProductRepository;

import jakarta.transaction.Transactional;

@Service
public class ProductServiceImpl implements ProductService {
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private ProductMapper productMapper;
	
	private static final Logger logger = LogManager.getLogger(ProductServiceImpl.class);
	
	@Autowired
    private InventoryClient inventoryClient;

	@Override
	@Transactional
	public ProductDTO addProduct(ProductDTO productDTO,String categoryName) {
		logger.info("Received request to add product: {} under category: {}", productDTO, categoryName);
		Optional<ProductEntity> opProduct=productRepository
											.findByProductName(productDTO.getProductName());
		
		if(opProduct.isPresent()) {
			logger.warn("Product already exists with name: {}", productDTO.getProductName());
			throw new ProductAlreadyExistsException(AppConstants.PRODUCT__ALREADY_EXISTS_NAME+
					productDTO.getProductName());
		}
		
		CategoryEntity category=categoryRepository
								 .findByCategoryName(categoryName)
								 .orElseGet(()-> {
									 logger.warn("Category not found with name: {}", categoryName);
									 throw new CategoryNotFoundException(AppConstants.CATEGORY_NOT_FOUND_NAME
									+categoryName);
									 });
								   
		ProductEntity product=productMapper.toEntity(productDTO);
		category.addPrdouct(product);
		product.setCategoryEntity(category);
	//	categoryRepository.save(category);
		logger.info("Adding product to database with ID: {}", product.getProductId());
		product=productRepository.save(product);
		
		logger.info("Successfully added product with ID: {}", product.getProductId());
		
		logger.info("Calling Inventory service to set set stock management for product");
		inventoryClient.addStock(product.getProductId(),  productDTO.getStock());
		logger.info("Called Inventory service and stock is well set for product with productId:"+product.getProductId());
		return productMapper.toDTO(product);
		
	}

	@Override
	public ProductDTO editProduct(ProductDTO productDTO) {
		logger.info("Received request to edit product: {}", productDTO);
		Optional<ProductEntity> op=productRepository.findById(productDTO.getProductId());
		if(op.isEmpty()) {
			logger.warn("Product not found with ID: {}", productDTO.getProductId());
			throw new ProductNotFoundException(AppConstants.PRODUCT_NOT_FOUND_ID
					+productDTO.getProductId());
		}
		else {
			ProductEntity product=op.get();
			product.setProductName(productDTO.getProductName());
			product.setProductDescription(productDTO.getProductDescription());
			product.setProductPrice(productDTO.getProductPrice());
			logger.info("Editing product with ID: {}", product.getProductId());
			product=productRepository.save(product);
			logger.info("Successfully edited product with ID: {}", product.getProductId());
			return productMapper.toDTO(product);
		}
		
	}
	
	@Transactional
	@Override
	public String deleteProduct(Integer productId) {
		logger.info("Received request to delete product with ID: {}", productId);
		Optional<ProductEntity> op = productRepository.findById(productId);
        if (op.isPresent()) {
        	ProductEntity product=op.get();
        	CategoryEntity category=product.getCategoryEntity();
        	if(category!=null) {
        		category.getProductsEntity().remove(product);
        		categoryRepository.save(category);
        	}
        	logger.info("Deleting product with ID: {}", productId);
            productRepository.delete(product);
            logger.info("Successfully deleted product with ID: {}", productId);
            return AppConstants.PRODUCT_DELETED;
        } else {
        	logger.warn("Product not found with ID: {}", productId);
            throw new ProductNotFoundException(AppConstants.PRODUCT_NOT_FOUND_ID + productId);
        }
						
	
	}

	@Override
	public ProductDTO getProductById(Integer productId) {
		logger.info("Received request to get product by ID: {}", productId);
		Optional<ProductEntity> op=productRepository.findById(productId);
		if(op.isEmpty()) {
			logger.error("Product not found with ID: {}", productId);
			throw new ProductNotFoundException(AppConstants.PRODUCT_NOT_FOUND_ID
							+productId);
		}
		else {
			ProductEntity product=op.get();
			logger.info("Successfully retrieved product with ID: {}", productId);
			return productMapper.toDTO(product);
		}
	}

	@Override
	public ProductDTO getProductByName(String productName) {
		logger.info("Received request to get product by name: {}", productName);
		Optional<ProductEntity> op=productRepository.findByProductName(productName);
		if(op.isEmpty()) {
			logger.error("Product not found with name: {}", productName);
			throw new ProductNotFoundException(AppConstants.PRODUCT_NOT_FOUND_NAME
							+productName);
		}
		else {
			ProductEntity product=op.get();
			logger.info("Successfully retrieved product with name: {}", productName);
			return productMapper.toDTO(product);
		}
	}

	@Override
	public List<ProductDTO> viewAllProducts() {
		logger.info("Received request to get all products");
		List<ProductEntity> list=productRepository.findAll();
		logger.info("Successfully retrieved {} products", list.size());
		return list.stream().map(productMapper::toDTO).toList();
	}

	@Override
	public List<ProductDTO> filterProductByCategory(String categoryName) {
		logger.info("Received request to filter products by category name: {}", categoryName);
		Optional<CategoryEntity> op=categoryRepository.findByCategoryName(categoryName);
		if(op.isEmpty()) {
			logger.warn("Category not found with name: {}", categoryName);
			throw new CategoryNotFoundException(AppConstants.CATEGORY_NOT_FOUND_NAME
					+categoryName);

		}
		logger.info("Filtering {} products under category: {}");
		List<ProductEntity> list=productRepository.findByCategoryEntityCategoryName(categoryName);
		logger.info("Successfully filtered {} products under category: {}", list.size(), categoryName);
		return list.stream().map(productMapper::toDTO).toList();
	}

	@Override
	@Transactional
	public ProductDTO updateStockForProduct(Integer productId, Integer newStock) {
		logger.info("Received request to update stock for product ID: {} with new stock: {}", productId, newStock);
		Optional<ProductEntity> productOpt = productRepository.findById(productId);
        if (productOpt.isPresent()) {
            ProductEntity product = productOpt.get();
            product.setStock(newStock);
            logger.info("updating stock for product ID: {}", productId);
            ProductEntity updatedProduct = productRepository.save(product);
            logger.info("Successfully updated stock for product ID: {}", productId);
            
            logger.info("calling inventory service to update stock ");
            inventoryClient.addStock(productId, newStock);
            logger.info("Stock is set for the product");
            return productMapper.toDTO(updatedProduct);
        } else {
        	logger.error("Product not found with ID: {}", productId);
            throw new ProductNotFoundException(AppConstants.PRODUCT_NOT_FOUND_ID+productId);
        }
	}

	@Override
	public boolean isProductValid(Integer productId) {
		logger.info("Received request to validate product with ID: {}", productId);
		Optional<ProductEntity> op=productRepository.findById(productId);
		if(op.isPresent()) {
			logger.info("Product with ID {} is valid", productId);
			return true;
			
		}
		else {
			logger.info("Product with ID {} is not valid", productId);
			return false;
		}
	}

}
