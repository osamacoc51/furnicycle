package com.furnicycle.inventoryservice.service;

import java.util.Date;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.furnicycle.inventoryservice.constants.AppConstants;
import com.furnicycle.inventoryservice.dto.InventoryDTO;
import com.furnicycle.inventoryservice.entity.InventoryEntity;
import com.furnicycle.inventoryservice.exception.InsufficientStockException;
import com.furnicycle.inventoryservice.exception.InventoryNotFoundException;
import com.furnicycle.inventoryservice.mapper.InventoryMapper;
import com.furnicycle.inventoryservice.repository.InventoryRepository;



@Service
public class InventoryServiceImpl implements InventoryService {
	
	@Autowired
    private InventoryRepository inventoryRepository;
	
	@Autowired
	private InventoryMapper inventoryMapper;
	
	private static final Logger logger = LogManager.getLogger(InventoryServiceImpl.class);
	
	
	@Override
	public InventoryDTO increaseAvailableStockAfterOrderCancel(Integer productId, Integer quantity) {
	    logger.info("Increasing {} reserved stock for product with ID: {}", quantity, productId);
	    InventoryEntity inventory = inventoryRepository.findByProductId(productId)
	            .orElseThrow(() -> new InventoryNotFoundException(AppConstants.INVENTORY_NOTFOUND + productId));

	    inventory.setAvailableStock(inventory.getAvailableStock() + quantity);
	    inventory.setLastUpdated(new Date());

	    logger.info("Available Stock increased for product ID: {}. New reserved stock: {}",
	            productId, inventory.getAvailableStock());

	    inventoryRepository.save(inventory);
	    return inventoryMapper.toDTO(inventory);
	}

	@Override
	public InventoryDTO addStock(Integer productId, Integer quantity) {
		
		logger.info("Adding {} stock to product with ID: {}", quantity, productId);
		InventoryEntity inventory = inventoryRepository.findByProductId(productId)
                .orElse(new InventoryEntity());
        inventory.setProductId(productId);
        inventory.setAvailableStock(inventory.getAvailableStock() + quantity);
        inventory.setLastUpdated(new Date());
        
        logger.info("Adding stock for product ID: {}. New available stock: {}", 
        		productId, inventory.getAvailableStock());
        inventoryRepository.save(inventory);
        logger.info("Stock added successfully for product ID: {}. New available stock: {}", 
        		productId, inventory.getAvailableStock());
        return inventoryMapper.toDTO(inventory);

	}

	@Override
	public InventoryDTO removeStock(Integer productId, Integer quantity) {
		logger.info("Removing {} stock from product with ID: {}", quantity, productId);
		InventoryEntity inventory = inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new InventoryNotFoundException(AppConstants.INVENTORY_NOTFOUND+ productId));
        
        if (inventory.getAvailableStock() < quantity) {
        	logger.warn("Insufficient stock for product ID: {}. Requested: {}, Available: {}",
        			productId, quantity, inventory.getAvailableStock());
            throw new InsufficientStockException(AppConstants.STOCK_NA);
        }
        
        inventory.setAvailableStock(inventory.getAvailableStock() - quantity);
        inventory.setLastUpdated(new Date());
        
        
        logger.info("Removing Stock  for product ID: {}. Remaining available stock: {}", 
        		productId, inventory.getAvailableStock());
        inventoryRepository.save(inventory);
        logger.info("Stock removed successfully for product ID: {}. Remaining available stock: {}", 
        		productId, inventory.getAvailableStock());
        return inventoryMapper.toDTO(inventory);
	}

	@Override
	public boolean reserveStockForOrder(Integer productId, Integer quantity) {
		 logger.info("Reserving {} stock for order for product with ID: {}", quantity, productId);
		 InventoryEntity inventory = inventoryRepository.findByProductId(productId)
	                .orElseThrow(() -> new InventoryNotFoundException(AppConstants.INVENTORY_NOTFOUND + productId));
	        
	        if (inventory.getAvailableStock() < quantity) {
	        	logger.warn("Not enough stock to reserve for product ID: {}. Requested: {}, Available: {}",
	        			productId, quantity, inventory.getAvailableStock());
	            return false; // Not enough stock to reserve
	        }
	        
	        inventory.setAvailableStock(inventory.getAvailableStock() - quantity);
	        inventory.setReservedStock(inventory.getReservedStock() + quantity);
	        inventory.setLastUpdated(new Date());
	        
	        logger.info("Reserving Stock for product ID: {}. Reserved stock: {}, Available stock: {}", 
	        		productId, inventory.getReservedStock(), inventory.getAvailableStock());
	        
	        inventoryRepository.save(inventory);
	        logger.info("Stock reserved successfully for product ID: {}. Reserved stock: {}, Available stock: {}", 
	        		productId, inventory.getReservedStock(), inventory.getAvailableStock());
	        return true;
	}

	@Override
	public boolean releaseReservedStock(Integer productId, Integer quantity) {
		logger.info("Releasing {} reserved stock for product with ID: {}", quantity, productId);
		InventoryEntity inventory = inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new InventoryNotFoundException(AppConstants.INVENTORY_NOTFOUND+ productId));

        if (inventory.getReservedStock() < quantity) {
        	logger.warn("Not enough reserved stock to release for product ID: {}. Requested: {}, Reserved: {}",
        			productId, quantity, inventory.getReservedStock());
            return false;
        }

        inventory.setReservedStock(inventory.getReservedStock() - quantity);
        inventory.setAvailableStock(inventory.getAvailableStock() + quantity);
        inventory.setLastUpdated(new Date());
        
        logger.info("Reserving stock for product ID: {}. Remaining reserved stock: {}, Available stock: {}",
        		productId, inventory.getReservedStock(), inventory.getAvailableStock());

        inventoryRepository.save(inventory);
        logger.info("Reserved stock released successfully for product ID: {}. Remaining reserved stock: {}, Available stock: {}",
        		productId, inventory.getReservedStock(), inventory.getAvailableStock());
        return true;
	}

	@Override
	public InventoryDTO reduceReservedStockAfterOrder(Integer productId, Integer quantity) {
		logger.info("Reducing {} reserved stock after order for product with ID: {}", quantity, productId);
		InventoryEntity inventory = inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new InventoryNotFoundException(AppConstants.INVENTORY_NOTFOUND+ productId));

        if (inventory.getReservedStock() < quantity) {
        	logger.warn("Not enough reserved stock to deduct for product ID: {}. Requested: {}, Reserved: {}",
        			productId, quantity, inventory.getReservedStock());
            throw new InsufficientStockException(AppConstants.RESERVEDSTOCK_NA);
        }

        inventory.setReservedStock(inventory.getReservedStock() - quantity);
        inventory.setLastUpdated(new Date());
        
        logger.info("Reducing Reserved Stock for order for product ID: {}. Remaining reserved stock: {}",
        		productId, inventory.getReservedStock());
        inventoryRepository.save(inventory);
        logger.info("Reserved stock reduced after order for product ID: {}. Remaining reserved stock: {}",
        		productId, inventory.getReservedStock());
        return inventoryMapper.toDTO(inventory);
	}

	@Override
	public InventoryDTO getInventoryByProductId(Integer productId) {
		logger.info("Fetching inventory for product ID: {}", productId);
		Optional<InventoryEntity> inventoryEntity = inventoryRepository.findByProductId(productId);

        if (inventoryEntity.isEmpty()) {
        	logger.warn("Inventory not found for product ID: {}", productId);
            throw new InventoryNotFoundException(AppConstants.INVENTORY_NOTFOUND + productId);
        }

        // Aggregate data if you have multiple warehouses
        logger.info("Inventory fetched for product ID: {}", productId);
        return inventoryMapper.toDTO(inventoryEntity.get());
	}
	
	
}
