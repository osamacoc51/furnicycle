package com.furnicycle.Ecommerce.controller;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.furnicycle.Ecommerce.dto.CartDTO;
import com.furnicycle.Ecommerce.dto.CartItemDTO;
import com.furnicycle.Ecommerce.service.CartService;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;
    
    private static final Logger logger = LogManager.getLogger(CartController.class);

    // Create a new cart for a customer
    @PostMapping("/create/{customerId}")
    public ResponseEntity<String> createCart(@PathVariable("customerId") Integer customerId) {
    	logger.info("Received request to create cart for customer ID: {}", customerId);
        String response = cartService.createCart(customerId);
        logger.info("Successfully created cart for customer ID: {}", customerId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Add items to a cart
    @PostMapping("/{cartId}/add/{productId}")
    public ResponseEntity<CartItemDTO> addItemToCart(@PathVariable("cartId") Integer cartId, 
                                                     @PathVariable("productId") Integer productId) {
    	logger.info("Received request to add product ID: {} to cart ID: {}", productId, cartId);
        CartItemDTO cartItemDto = cartService.addItemsToCart(cartId, productId);
        logger.info("Successfully added product ID: {} to cart ID: {}", productId, cartId);
        return ResponseEntity.status(HttpStatus.OK).body(cartItemDto);
    }

    // Remove items from a cart
    @DeleteMapping("/{cartId}/remove/{productId}")
    public ResponseEntity<CartItemDTO> removeItemFromCart(@PathVariable("cartId") Integer cartId, 
                                                          @PathVariable("productId") Integer productId) {
    	logger.info("Received request to remove product ID: {} from cart ID: {}", productId, cartId);
        CartItemDTO cartItemDto = cartService.removeItemsFromCart(cartId, productId);
        logger.info("Successfully removed product ID: {} from cart ID: {}", productId, cartId);
        return ResponseEntity.status(HttpStatus.OK).body(cartItemDto);
    }

    // Modify item quantity in the cart
    @PutMapping("/{cartId}/modify/{productId}/{quantity}")
    public ResponseEntity<CartItemDTO> modifyItemQuantity(@PathVariable("cartId") Integer cartId, 
                                                          @PathVariable("productId") Integer productId,
                                                          @PathVariable("quantity") Integer quantity) {
    	logger.info("Received request to modify quantity of product ID: {} in cart ID: {} to {}", productId, cartId, quantity);
        CartItemDTO cartItemDto = cartService.modifyQuantity(cartId, productId, quantity);
        logger.info("Successfully modified quantity of product ID: {} in cart ID: {} to {}", productId, cartId, quantity);
        return ResponseEntity.status(HttpStatus.OK).body(cartItemDto);
    }

    // Clear the cart
    @DeleteMapping("/{cartId}/clear")
    public ResponseEntity<String> clearCart(@PathVariable("cartId") Integer cartId) {
    	logger.info("Received request to clear cart with ID: {}", cartId);
        String message = cartService.clearCart(cartId);
        logger.info("Successfully cleared cart with ID: {}", cartId);
        return ResponseEntity.status(HttpStatus.OK).body(message);
    }

    // Get cart by customer ID
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<CartDTO> getCartByCustomerId(@PathVariable("customerId") Integer customerId) {
    	logger.info("Received request to get cart for customer ID: {}", customerId);
        CartDTO cartDto = cartService.getCartByCustomerId(customerId);
        logger.info("Successfully retrieved cart for customer ID: {}", customerId);
        return ResponseEntity.status(HttpStatus.OK).body(cartDto);
    }

    // Get cart by cart ID
    @GetMapping("/{cartId}")
    public ResponseEntity<CartDTO> getCartById(@PathVariable("cartId") Integer cartId) {
    	logger.info("Received request to get cart by ID: {}", cartId);
        CartDTO cartDto = cartService.getCartById(cartId);
        logger.info("Successfully retrieved cart with ID: {}", cartId);
        return ResponseEntity.status(HttpStatus.OK).body(cartDto);
    }
}
