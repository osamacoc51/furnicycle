package com.furnicycle.inventoryservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.furnicycle.inventoryservice.constants.AppConstants;
import com.furnicycle.inventoryservice.dto.InventoryDTO;
import com.furnicycle.inventoryservice.service.InventoryService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


@RestController
@RequestMapping("/inventory")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;
    
    private static final Logger logger = LogManager.getLogger(InventoryController.class);
    
    @PutMapping("/increaseAvailableStock")
    public ResponseEntity<InventoryDTO> increaseAvailableStockAfterOrderCancel(
            @RequestParam("productId") Integer productId,
            @RequestParam("quantity") Integer quantity) {

        logger.info("Request to increase available stock for product ID: {} by {}", productId, quantity);

        InventoryDTO updatedInventory = inventoryService.increaseAvailableStockAfterOrderCancel(productId, quantity);

        logger.info("Available stock increased successfully for product ID: {}", productId);

        return new ResponseEntity<>(updatedInventory, HttpStatus.OK);
    }

    // POST or PUT to add stock to a product's inventory
    @PostMapping("/{productId}/add")
    public ResponseEntity<InventoryDTO> addStock(
            @PathVariable("productId") Integer productId, 
            @RequestParam("quantity") Integer quantity) {
    	
    	logger.info("Request to add {} stock for product ID: {}", quantity, productId);

        InventoryDTO updatedInventory = inventoryService.addStock(productId, quantity);
        
        logger.info("Stock added successfully for product ID: {}", productId);
        return ResponseEntity.status(HttpStatus.CREATED)
        					.contentType(MediaType.APPLICATION_JSON)
        					.body(updatedInventory);
    }

    // POST or PUT to remove stock from a product's inventory
    @PostMapping("/{productId}/remove")
    public ResponseEntity<InventoryDTO> removeStock(
            @PathVariable("productId") Integer productId, 
            @RequestParam("quantity") Integer quantity) {
    	
    	logger.info("Request to remove {} stock for product ID: {}", quantity, productId);
        InventoryDTO updatedInventory = inventoryService.removeStock(productId, quantity);
        
        logger.info("Stock removed successfully for product ID: {}", productId);
        return ResponseEntity.status(HttpStatus.OK)
        					.contentType(MediaType.APPLICATION_JSON)
        					.body(updatedInventory);
    }

    // POST to reserve stock for an order
    @PostMapping("/{productId}/reserve")
    public ResponseEntity<String> reserveStockForOrder(
            @PathVariable("productId") Integer productId, 
            @RequestParam("quantity") Integer quantity) {
    	
    	logger.info("Request to reserve {} stock for product ID: {}", quantity, productId);
        boolean success = inventoryService.reserveStockForOrder(productId, quantity);
        if (success) {
        	logger.info("Stock reserved successfully for product ID: {}", productId);
            return ResponseEntity.status(HttpStatus.OK)
            		.contentType(MediaType.APPLICATION_JSON)
            		.body(AppConstants.STOCK_RESERVED);
        } else {
        	logger.warn("Insufficient stock to reserve for product ID: {}", productId);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            		.contentType(MediaType.APPLICATION_JSON)
            		.body(AppConstants.STOCK_INSUFFICIENT);
        }
    }

    
    @PostMapping("/{productId}/release")
    public ResponseEntity<String> releaseReservedStock(
            @PathVariable("productId") Integer productId, 
            @RequestParam("quantity") Integer quantity) {
    	logger.info("Request to release {} reserved stock for product ID: {}", quantity, productId);
        boolean success = inventoryService.releaseReservedStock(productId, quantity);
        if (success) {
        	logger.info("Reserved stock released successfully for product ID: {}", productId);
            return ResponseEntity.status(HttpStatus.OK)
            		.contentType(MediaType.APPLICATION_JSON)
            		.body(AppConstants.RESERVED_STOCK_RELEASED);
        } else {
        	logger.warn("Not enough reserved stock to release for product ID: {}", productId);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            		.contentType(MediaType.APPLICATION_JSON)
            		.body(AppConstants.RESERVED_STOCK_NOTRELEASED);
        }
    }

    // POST to reduce reserved stock after order confirmation
    @PostMapping("/{productId}/reduce")
    public ResponseEntity<InventoryDTO> reduceReservedStockAfterOrder(
            @PathVariable("productId") Integer productId, 
            @RequestParam("quantity") Integer quantity) {
    	logger.info("Request to reduce {} reserved stock after order for product ID: {}", quantity, productId);
        InventoryDTO updatedInventory = inventoryService.reduceReservedStockAfterOrder(productId, quantity);
        
        logger.info("Reserved stock reduced successfully for product ID: {}", productId);
        return ResponseEntity.status(HttpStatus.OK)
        		.contentType(MediaType.APPLICATION_JSON)
        		.body(updatedInventory);
    }

    // GET to fetch the current stock for a product
    @GetMapping("/{productId}")
    public ResponseEntity<InventoryDTO> getInventoryByProductId(
            @PathVariable("productId") Integer productId) {
    	
    	logger.info("Request to fetch inventory for product ID: {}", productId);
        InventoryDTO inventoryDTO = inventoryService.getInventoryByProductId(productId);
        
        logger.info("Inventory fetched successfully for product ID: {}", productId);
        return ResponseEntity.status(HttpStatus.OK)
        		.contentType(MediaType.APPLICATION_JSON)
        		.body(inventoryDTO);
    }
}
