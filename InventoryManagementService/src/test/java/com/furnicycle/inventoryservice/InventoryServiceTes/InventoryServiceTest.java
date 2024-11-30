package com.furnicycle.inventoryservice.InventoryServiceTes;


import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.furnicycle.inventoryservice.constants.AppConstants;
import com.furnicycle.inventoryservice.dto.InventoryDTO;
import com.furnicycle.inventoryservice.entity.InventoryEntity;
import com.furnicycle.inventoryservice.exception.InsufficientStockException;
import com.furnicycle.inventoryservice.exception.InventoryNotFoundException;
import com.furnicycle.inventoryservice.mapper.InventoryMapper;
import com.furnicycle.inventoryservice.repository.InventoryRepository;
import com.furnicycle.inventoryservice.service.InventoryServiceImpl;

@ExtendWith(MockitoExtension.class)
public class InventoryServiceTest {

		
		@Mock
		private InventoryRepository inventoryRepository;
		
		@Mock
		private InventoryMapper inventoryMapper;
		
		@InjectMocks
		private InventoryServiceImpl inventoryService;
		
		private InventoryEntity inventoryEntity;
		private InventoryDTO inventoryDTO;
		
		@BeforeEach
		public void setup() {
			inventoryEntity=new InventoryEntity();
			inventoryEntity.setInventoryId(1);
			inventoryEntity.setAvailableStock(5);
			inventoryEntity.setReservedStock(2);
			inventoryEntity.setProductId(1);
			
			inventoryDTO=new InventoryDTO();
			inventoryDTO.setInventoryId(1);
			inventoryDTO.setAvailableStock(5);
			inventoryDTO.setReservedStock(0);
			inventoryDTO.setProductId(1);
		}
		
		@AfterEach
		public void teardown() {
			inventoryDTO=null;
			inventoryEntity=null;
		}
		
		@Test
		public void addStock_Success_Test() {
			when(inventoryRepository.findByProductId(anyInt()))
			.thenReturn(Optional.of(inventoryEntity));
			
			when(inventoryRepository.save(any(InventoryEntity.class)))
			.thenReturn(inventoryEntity);
			
			when(inventoryMapper.toDTO(inventoryEntity))
			.thenReturn(inventoryDTO);
			
			InventoryDTO result=inventoryService.addStock(1, 5);
			
			assertNotNull(result);
			assertEquals(result.getInventoryId(),inventoryDTO.getInventoryId());
			assertEquals(result.getProductId(), inventoryDTO.getProductId());
			assertEquals(result.getAvailableStock(),inventoryDTO.getAvailableStock());
			assertEquals(result.getReservedStock(),inventoryDTO.getReservedStock());
			
			verify(inventoryRepository, times(1)).findByProductId(1);
			verify(inventoryRepository, times(1)).save(any(InventoryEntity.class));
				
		}
		
		@Test
		public void increaseAvailableStockAfterOrderCancel__Success_Test() {
			when(inventoryRepository.findByProductId(anyInt()))
			.thenReturn(Optional.of(inventoryEntity));
			when(inventoryRepository.save(any(InventoryEntity.class))).thenReturn(inventoryEntity);
			
			when(inventoryMapper.toDTO(inventoryEntity)).thenReturn(inventoryDTO);
			
			InventoryDTO result=inventoryService.increaseAvailableStockAfterOrderCancel(1, 3);
			assertNotNull(result);
			assertEquals(result.getInventoryId(),inventoryDTO.getInventoryId());
			assertEquals(result.getProductId(), inventoryDTO.getProductId());
			assertEquals(result.getAvailableStock(),inventoryDTO.getAvailableStock());
			assertEquals(result.getReservedStock(),inventoryDTO.getReservedStock());
			
			verify(inventoryRepository, times(1)).findByProductId(1);
			verify(inventoryRepository, times(1)).save(any(InventoryEntity.class));
		}
		
		@Test
		public void increaseAvailableStockAfterOrderCancel_InventoryNotFound_Test() {
			when(inventoryRepository.findByProductId(22))
			.thenReturn(Optional.empty());
	 
			assertThatThrownBy(()->inventoryService.increaseAvailableStockAfterOrderCancel(22, 3))
			.isInstanceOf(InventoryNotFoundException.class)
			.hasMessage(AppConstants.INVENTORY_NOTFOUND+22);
			
			
			verify(inventoryRepository,times(1)).findByProductId(22);
			verify(inventoryRepository, never()).save(any(InventoryEntity.class));
		}
		
		@Test
		public void removeStock_Test() {
			when(inventoryRepository.findByProductId(anyInt()))
			.thenReturn(Optional.of(inventoryEntity));	
				when(inventoryRepository.save(any(InventoryEntity.class))).thenReturn(inventoryEntity);	
				when(inventoryMapper.toDTO(inventoryEntity)).thenReturn(inventoryDTO);
			
			InventoryDTO result=inventoryService.removeStock(1, 3);
			
			assertNotNull(result);
			assertEquals(result.getInventoryId(),inventoryDTO.getInventoryId());
			assertEquals(result.getProductId(), inventoryDTO.getProductId());
			assertEquals(result.getAvailableStock(),inventoryDTO.getAvailableStock());
			assertEquals(result.getReservedStock(),inventoryDTO.getReservedStock());
			
			verify(inventoryRepository, times(1)).findByProductId(1);
			verify(inventoryRepository, times(1)).save(any(InventoryEntity.class));
		}
		
		@Test
		public void removeStock_InventoryNotFound_Test() {
			when(inventoryRepository.findByProductId(22))
			.thenReturn(Optional.empty());
	 
			assertThatThrownBy(()->inventoryService.removeStock(22, 2))
			.isInstanceOf(InventoryNotFoundException.class)
			.hasMessage(AppConstants.INVENTORY_NOTFOUND+22);
			
			
			verify(inventoryRepository,times(1)).findByProductId(22);
			verify(inventoryRepository, never()).save(any(InventoryEntity.class));
		}
		
		@Test
		public void removeStock_InsufficientStock_Test() {
			when(inventoryRepository.findByProductId(22))
			.thenReturn(Optional.of(inventoryEntity));
	 
			assertThatThrownBy(()->inventoryService.removeStock(22, 20))
			.isInstanceOf(InsufficientStockException.class)
			.hasMessage(AppConstants.STOCK_NA);
			
			
			verify(inventoryRepository,times(1)).findByProductId(22);
			verify(inventoryRepository, never()).save(any(InventoryEntity.class));
		}
		
		@Test
		public void reserveStockForOrder_Test() {
			when(inventoryRepository.findByProductId(1))
			.thenReturn(Optional.of(inventoryEntity));
			
			when(inventoryRepository.save(any(InventoryEntity.class))).thenReturn(inventoryEntity);	
			boolean result=inventoryService.reserveStockForOrder(1, 3);
			
			assertTrue(result);
			
			verify(inventoryRepository,times(1)).findByProductId(1);
			verify(inventoryRepository,times(1)).save(inventoryEntity);
		}
		
		@Test
		public void reserveStockForOrder_InventoryNotFound_Test() {
			when(inventoryRepository.findByProductId(22))
			.thenReturn(Optional.empty());
			
			assertThatThrownBy(()->inventoryService.reserveStockForOrder(22, 2))
			.isInstanceOf(InventoryNotFoundException.class)
			.hasMessage(AppConstants.INVENTORY_NOTFOUND+22);
			
			
			verify(inventoryRepository,times(1)).findByProductId(22);
			verify(inventoryRepository, never()).save(any(InventoryEntity.class));
		}
		
		@Test
		public void reserveStockForOrder_Stock_NA_Test() {
			when(inventoryRepository.findByProductId(1))
			.thenReturn(Optional.of(inventoryEntity));
			
			boolean result=inventoryService.reserveStockForOrder(1, 15);
			
			assertFalse(result);
			
			verify(inventoryRepository,times(1)).findByProductId(1);
			verify(inventoryRepository,never()).save(inventoryEntity);
		}
		
		@Test
		public void releaseReservedStock_Success_test() {
			when(inventoryRepository.findByProductId(1))
			.thenReturn(Optional.of(inventoryEntity));
			
			when(inventoryRepository.save(any(InventoryEntity.class)))
			.thenReturn(inventoryEntity);
			
			boolean result=inventoryService.reserveStockForOrder(1, 1);
			
			assertTrue(result);
			
			verify(inventoryRepository,times(1)).findByProductId(1);
			verify(inventoryRepository,times(1)).save(any(InventoryEntity.class));
		}
		
		@Test
		public void releaseReservedStock_inventoryNotFound() {
			when(inventoryRepository.findByProductId(22))
			.thenReturn(Optional.empty());
			
			assertThatThrownBy(()->inventoryService.releaseReservedStock(22, 3))
			.isInstanceOf(InventoryNotFoundException.class)
			.hasMessage(AppConstants.INVENTORY_NOTFOUND+22);
			
			verify(inventoryRepository,times(1)).findByProductId(22);
			verify(inventoryRepository,never()).save(any(InventoryEntity.class));
		}
		
		@Test
		public void releaseReservedStock_ReservedStock_NotReleased() {
			when(inventoryRepository.findByProductId(1))
			.thenReturn(Optional.of(inventoryEntity));
			
			
			boolean result=inventoryService.reserveStockForOrder(1, 11);
			
			assertFalse(result);
			
			verify(inventoryRepository,times(1)).findByProductId(1);
			verify(inventoryRepository,never()).save(any(InventoryEntity.class));
		}
		
		@Test
		public void reduceReservedStockAfterOrder_Success_test() {
			when(inventoryRepository.findByProductId(1))
			.thenReturn(Optional.of(inventoryEntity));
			
			when(inventoryRepository.save(any(InventoryEntity.class)))
			.thenReturn(inventoryEntity);
			
			when(inventoryMapper.toDTO(inventoryEntity))
			.thenReturn(inventoryDTO);
			
			InventoryDTO result=inventoryService.removeStock(1, 3);
			
			assertNotNull(result);
			assertEquals(result.getInventoryId(),inventoryDTO.getInventoryId());
			assertEquals(result.getProductId(), inventoryDTO.getProductId());
			assertEquals(result.getAvailableStock(),inventoryDTO.getAvailableStock());
			assertEquals(result.getReservedStock(),inventoryDTO.getReservedStock());
			
			verify(inventoryRepository, times(1)).findByProductId(1);
			verify(inventoryRepository, times(1)).save(any(InventoryEntity.class));
			
		}
		
		@Test
		public void reduceReservedStockAfterOrder_InventoryNotFound_test() {
			when(inventoryRepository.findByProductId(22))
			.thenReturn(Optional.empty());
			
			assertThatThrownBy(()->inventoryService.releaseReservedStock(22, 3))
			.isInstanceOf(InventoryNotFoundException.class)
			.hasMessage(AppConstants.INVENTORY_NOTFOUND+22);
			
			verify(inventoryRepository,times(1)).findByProductId(22);
			verify(inventoryRepository,never()).save(any(InventoryEntity.class));
		}
		
		@Test
		public void reduceReservedStockAfterOrder_InsufficientStock_test() {
			when(inventoryRepository.findByProductId(1))
			.thenReturn(Optional.of(inventoryEntity));
			
			assertThatThrownBy(()->inventoryService.reduceReservedStockAfterOrder(1, 11))
			.isInstanceOf(InsufficientStockException.class)
			.hasMessage(AppConstants.RESERVEDSTOCK_NA);
			
			verify(inventoryRepository,times(1)).findByProductId(1);
			verify(inventoryRepository,never()).save(any(InventoryEntity.class));
		}
		
		@Test
		public void getInventoryByProductId_Success_Test() {
			when(inventoryRepository.findByProductId(1))
			.thenReturn(Optional.of(inventoryEntity));
			
			when(inventoryMapper.toDTO(inventoryEntity))
			.thenReturn(inventoryDTO);
			
			InventoryDTO result=inventoryService.getInventoryByProductId(1);
			
			assertNotNull(result);
			assertEquals(result.getInventoryId(),inventoryDTO.getInventoryId());
			assertEquals(result.getProductId(), inventoryDTO.getProductId());
			assertEquals(result.getAvailableStock(),inventoryDTO.getAvailableStock());
			assertEquals(result.getReservedStock(),inventoryDTO.getReservedStock());
			
			verify(inventoryRepository, times(1)).findByProductId(1);
			
		}
		
		@Test
		public void getProductById_inventoryNotFound() {
			when(inventoryRepository.findByProductId(22))
			.thenReturn(Optional.empty());
			
			assertThatThrownBy(()->inventoryService.getInventoryByProductId(22))
			.isInstanceOf(InventoryNotFoundException.class)
			.hasMessage(AppConstants.INVENTORY_NOTFOUND+22);
			
			verify(inventoryRepository,times(1)).findByProductId(22);
		}
		
		
		
		
	 
	}