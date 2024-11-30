package com.furnicycle.Ecommerce.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.furnicycle.Ecommerce.constants.AppConstants;
import com.furnicycle.Ecommerce.dto.CartDTO;
import com.furnicycle.Ecommerce.dto.CartItemDTO;
import com.furnicycle.Ecommerce.dto.InventoryDTO;
import com.furnicycle.Ecommerce.entity.CartEntity;
import com.furnicycle.Ecommerce.entity.CartItemEntity;
import com.furnicycle.Ecommerce.exception.CartNotFoundException;
import com.furnicycle.Ecommerce.exception.InsufficientStockException;
import com.furnicycle.Ecommerce.exception.InvalidProductException;
import com.furnicycle.Ecommerce.exception.ProductServiceDownException;
import com.furnicycle.Ecommerce.feign.InventoryServiceClient;
import com.furnicycle.Ecommerce.feign.ProductServiceClient;
import com.furnicycle.Ecommerce.mapper.CartItemMapper;
import com.furnicycle.Ecommerce.mapper.CartMapper;
import com.furnicycle.Ecommerce.repository.CartItemRepository;
import com.furnicycle.Ecommerce.repository.CartRepository;
import com.furnicycle.Ecommerce.service.CartService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.transaction.Transactional;

@Service
public class CartServiceImpl implements CartService {

	@Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

   

    @Autowired
    @Lazy
    private ProductServiceClient productServiceClient;
    
    @Autowired
    private InventoryServiceClient inventoryServiceClient; 

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private CartItemMapper cartItemMapper;
    
    private static final Logger logger = LogManager.getLogger(CartServiceImpl.class);
    
    @Override
    @Transactional
    public String createCart(Integer customerId) {
    	logger.info("Received request to create cart for customer ID: {}", customerId);
        

        CartEntity cartEntity = new CartEntity();
        cartEntity.setCreationDate(new Date());
        cartEntity.setCustomerId(customerId);
        
        logger.info("Saving cart with ID: {}"+customerId);
        CartEntity savedCart = cartRepository.save(cartEntity);
        logger.info("Successfully created cart with ID: {}", savedCart.getCartId());
        return AppConstants.CART_SUCCESSFUL+customerId;
    }
    
   

    @Override
    @Transactional
    @CircuitBreaker(name = "productServiceCircuitBreaker", fallbackMethod = "fallbackForProductValidation")
    public CartItemDTO addItemsToCart(Integer cartId, Integer productId) {
    	logger.info("Received request to add product ID: {} to cart ID: {}", productId, cartId);
        if (!productServiceClient.isValidProduct(productId)) {
        	logger.error("Invalid product ID: {}", productId);
            throw new InvalidProductException(AppConstants.PRODUCT_NOTFOUND+productId);
        }

        Optional<CartEntity> cartOptional = cartRepository.findById(cartId);
        if (!cartOptional.isPresent()) {
        	logger.error("Cart not found with ID: {}", cartId);
            throw new CartNotFoundException(AppConstants.CART_NOTFOUND+cartId);
        }

        CartEntity cartEntity = cartOptional.get();

        // Check if the item already exists in the cart
        Optional<CartItemEntity> existingCartItem = cartEntity.getCartItemEntities().stream()
                .filter(item -> item.getProductId().equals(productId))
                .findFirst();

        
        CartItemEntity cartItemEntity;
        if (existingCartItem.isPresent()) {
            cartItemEntity = existingCartItem.get();
            logger.info("Increasing quantity of product ID: {} in cart ID: {}", productId, cartId);
            cartItemEntity.setQuantity(cartItemEntity.getQuantity() + 1);
            logger.info("Increased quantity of product ID: {} in cart ID: {}", productId, cartId);
        } else {
            cartItemEntity = new CartItemEntity();
            cartItemEntity.setProductId(productId);
            cartItemEntity.setQuantity(1);
            cartItemEntity.setCartEntity(cartEntity);
            logger.info("Adding new product ID: {} to cart ID: {}", productId, cartId);
            cartEntity.getCartItemEntities().add(cartItemEntity);
            logger.info("Added new product ID: {} to cart ID: {}", productId, cartId);
        }
        
        logger.info("Calling Inventory service to check how much stock is there before adding to cart");
        InventoryDTO inventory = inventoryServiceClient.getInventoryByProductId(productId);
        if (inventory.getAvailableStock() < cartItemEntity.getQuantity()) {
        	logger.warn("Insufficient stock Available stock:"+inventory.getAvailableStock()+
        			"You are trying to add:"+cartItemEntity.getQuantity());
            throw new InsufficientStockException(AppConstants.INSUFFICIENT_STOCK + productId);
        }
        logger.info("Calling Inventory Service to Reserve stock as enough stock is present");
        inventoryServiceClient.reserveStockForOrder(productId, 1);
        logger.info("Reserved stock for product as you are adding to cart");
        
        logger.info("Saving Cart to database  with cart ID: {}", cartId);
        CartItemEntity savedCartItem = cartItemRepository.save(cartItemEntity);
        logger.info("Saved Cart to database  with cart ID: {}", cartId);
        // Fetch the saved entity to ensure cartItemId is populated
//        CartItemEntity fetchedCartItem = cartItemRepository.findById(savedCartItem.getCartItemId())
//                .orElseThrow(() -> new RuntimeException("Failed to retrieve saved CartItemEntity"));
        
        
        return cartItemMapper.toDto(savedCartItem);

    }
    
    public CartItemDTO fallbackForProductValidation(Integer cartId, Integer productId, Throwable t) {
        logger.error("Fallback triggered for product validation for product ID: {} and cart ID: {}", productId, cartId);
        // Return a default response or throw a custom exception
        throw new ProductServiceDownException(AppConstants.PRODUCT_SERVICE_DOWN);
    }
    
    @Override
    @Transactional
    public CartItemDTO removeItemsFromCart(Integer cartId, Integer productId) {
    	logger.info("Received request to remove product ID: {} from cart ID: {}", productId, cartId);
        Optional<CartEntity> cartOptional = cartRepository.findById(cartId);
        if (!cartOptional.isPresent()) {
        	logger.warn("Cart not found with ID: {}", cartId);
            throw new CartNotFoundException(AppConstants.CART_NOTFOUND+cartId);
        }

        CartEntity cartEntity = cartOptional.get();
        List<CartItemEntity> itemsToRemove = cartEntity.getCartItemEntities().stream()
                .filter(item -> item.getProductId().equals(productId))
                .collect(Collectors.toList());

        if (itemsToRemove.isEmpty()) {
        	logger.warn("Product ID: {} not found in cart ID: {}", productId, cartId);
            throw new InvalidProductException(AppConstants.PRODUCT_NOTFOUND+productId);
        }
        
        logger.info("Calling inventory service to release stock quantity as you are removing item from cart");
        inventoryServiceClient.releaseReservedStock(productId ,cartEntity.getCartItemEntities().get(0).getQuantity());
        logger.info("Released stock quantity as you have removed item from cart");
        
        logger.info("Removing cartItem from cart");
        cartEntity.getCartItemEntities().removeAll(itemsToRemove);
        logger.info("Removing product ID: {} from cart ID: {}", productId, cartId);
        cartItemRepository.deleteAll(itemsToRemove);
        
        
        
        
        
        cartRepository.save(cartEntity);
        logger.info("Removed product ID: {} from cart ID: {}", productId, cartId);
        return cartItemMapper.toDto(itemsToRemove.get(0));  // Returning the removed item, modify if needed
    }

    @Override
    @Transactional
    public CartItemDTO modifyQuantity(Integer cartId, Integer productId, Integer quantity) {
    	logger.info("Received request to modify quantity of product ID: {} in cart ID: {} to {}", productId, cartId, quantity);
        

        Optional<CartEntity> cartOptional = cartRepository.findById(cartId);
        if (!cartOptional.isPresent()) {
        	logger.warn("Cart not found with ID: {}", cartId);
            throw new CartNotFoundException(AppConstants.CART_NOTFOUND+cartId);
        }

        CartEntity cartEntity = cartOptional.get();
        CartItemEntity cartItemEntity = cartEntity.getCartItemEntities().stream()
                .filter(item -> item.getProductId().equals(productId))
                .findFirst()
                .orElseGet(() ->{
                	logger.error("Product ID: {} not found in cart ID: {}", productId, cartId);
                throw new InvalidProductException(AppConstants.PRODUCT_NOTFOUND+productId);
                });
        int currentQuantity = cartItemEntity.getQuantity();
        if (quantity <= 0) {
            return removeItemsFromCart(cartId, productId);
        }
        
        logger.info("Calling Inventory service to check stock before modifying quantity");
        InventoryDTO inventory = inventoryServiceClient.getInventoryByProductId(productId);
        
        if (inventory.getAvailableStock() < quantity) {
            logger.warn("Insufficient stock Available stock: {}. You are trying to add: {}", 
                        inventory.getAvailableStock(), quantity);
            throw new InsufficientStockException(AppConstants.INSUFFICIENT_STOCK + productId);
        }
        
        // Adjust reserved stock based on new quantity
        if (quantity > currentQuantity) {
            int additionalStockRequired = quantity - currentQuantity;
            logger.info("Reserving additional stock for product ID: {}", productId);
            inventoryServiceClient.reserveStockForOrder(productId, additionalStockRequired);
            logger.info("Reserved additional stock for product ID: {}", productId);
        } else if (quantity < currentQuantity) {
            int stockToRelease = currentQuantity - quantity;
            logger.info("Releasing excess reserved stock for product ID: {}", productId);
            inventoryServiceClient.releaseReservedStock(productId, stockToRelease);
            logger.info("Released excess reserved stock for product ID: {}", productId);
        }

        
        
        logger.info("Updating quantity of product ID: {} in cart ID: {} to {}", productId, cartId, quantity);
        cartItemEntity.setQuantity(quantity);
        logger.info("Updated quantity of product ID: {} in cart ID: {} to {}", productId, cartId, quantity);
        return cartItemMapper.toDto(cartItemRepository.save(cartItemEntity));
    }

    @Override
    @Transactional
    public String clearCart(Integer cartId) {
    	logger.info("Received request to clear cart with ID: {}", cartId);
    	Optional<CartEntity> cartOptional = cartRepository.findById(cartId);
        if (!cartOptional.isPresent()) {
        	logger.error("Cart not found with ID: {}", cartId);
            throw new CartNotFoundException(AppConstants.CART_NOTFOUND+cartId);
        }

        CartEntity cartEntity = cartOptional.get();
        
        logger.info("Releasing reserved stock for all items in cart ID: {}", cartId);
        for (CartItemEntity cartItem : cartEntity.getCartItemEntities()) {
            inventoryServiceClient.releaseReservedStock(cartItem.getProductId(), cartItem.getQuantity());
            logger.info("Released reserved stock for product ID: {}", cartItem.getProductId());
        }
        
        cartEntity.getCartItemEntities().clear();
        logger.info("Clearing cart with ID: {}", cartId);
        cartRepository.save(cartEntity);
        
        logger.info("Cleared cart with ID: {}", cartId);
        return AppConstants.CLEAR_CART;
    }

    @Override
    public CartDTO getCartByCustomerId(Integer customerId) {
    	logger.info("Received request to get cart for customer ID: {}", customerId);
        Optional<CartEntity> op = cartRepository.findByCustomerId(customerId);
        if (op.isEmpty()) {
        	logger.error("Cart not found for customer ID: {}", customerId);
            throw new CartNotFoundException(AppConstants.CART_NOTFOUND_FOR_CUSTOMER+customerId);
        }
        logger.info("Successfully retrieved cart for customer ID: {}", customerId);
        return cartMapper.toDto(op.get());
    }

    @Override
    public CartDTO getCartById(Integer cartId) {
    	logger.info("Received request to get cart by ID: {}", cartId);
        CartEntity cartEntity = cartRepository.findById(cartId)
                .orElseGet(() -> {
                	logger.error("Cart not found with ID: {}", cartId);
                    throw new CartNotFoundException(AppConstants.CART_NOTFOUND+cartId);
                });
        logger.info("Successfully retrieved cart with ID: {}", cartId);
        return cartMapper.toDto(cartEntity);
    }
}

