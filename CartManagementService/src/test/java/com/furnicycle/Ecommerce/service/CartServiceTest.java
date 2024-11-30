package com.furnicycle.Ecommerce.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
import com.furnicycle.Ecommerce.service.impl.CartServiceImpl;

@ExtendWith(MockitoExtension.class)
public class CartServiceTest {
 
	@Mock
	private CartRepository cartRepository;
	
	@Mock
	private CartItemRepository cartItemRepository;
	
	@Mock
	private CartMapper cartMapper;
	
	@Mock
	private CartItemMapper cartItemMapper;
	
	@Mock
	private InventoryServiceClient inventoryServiceClient;
	
	@Mock
	private ProductServiceClient productServiceClient;
	
	@InjectMocks
	private CartServiceImpl cartService;
	
	private CartEntity cartEntity;
	private CartItemEntity cartItemEntity;
	private CartDTO cartDTO;
	private CartItemDTO cartItemDTO;
	
	List<CartItemEntity> list;
	List<CartItemDTO> dtoList;
	
	@BeforeEach
	public void setUp() {
		cartEntity=new CartEntity();
		cartEntity.setCreationDate(new Date());
		cartEntity.setCustomerId(1);
		
		cartItemEntity=new CartItemEntity();
		cartItemEntity.setProductId(1);
		cartItemEntity.setQuantity(2);
		
		list=new ArrayList<>();
		list.add(cartItemEntity);
		
		cartEntity.setCartItemEntities(list);
		cartItemEntity.setCartEntity(cartEntity);
		
		cartDTO=new CartDTO();
		cartDTO.setCreationDate(new Date());
		cartDTO.setCustomerId(1);
		
		cartItemDTO=new CartItemDTO();
		cartItemDTO.setProductId(1);
		cartItemDTO.setQuantity(2);		
		
		dtoList=new ArrayList<>();
		dtoList.add(cartItemDTO);
		
		cartDTO.setCartItemDtos(dtoList);
		cartItemDTO.setCartDTO(cartDTO);
	}
	
	@AfterEach
	public void tearDown() {
		cartEntity=null;
		cartItemEntity=null;
		cartDTO=null;
		cartItemDTO=null;
	}
	
	@Test
	public void CreateCart_Success_Test() {
		
		when(cartRepository.save(any(CartEntity.class))).thenReturn(cartEntity);
		
		
		String result=cartService.createCart(1);
		
		assertEquals(result, AppConstants.CART_SUCCESSFUL+cartEntity.getCustomerId());
		
		verify(cartRepository , times(1)).save(any(CartEntity.class));
	}
	
	
	
	@Test
	public void addItemsToCart_ExistingItem_Success_Test() {
		Integer cartId = 1;
        Integer productId = 101;
 
        when(productServiceClient.isValidProduct(productId)).thenReturn(true);
        when(cartRepository.findById(cartId)).thenReturn(Optional.of(cartEntity));
        
        InventoryDTO inventory=new InventoryDTO(1,1,3,0,null);
        
        when(inventoryServiceClient.getInventoryByProductId(productId))
        .thenReturn(inventory);
        
        doNothing().when(inventoryServiceClient).reserveStockForOrder(anyInt(),anyInt());
        
        when(cartItemRepository.save(any(CartItemEntity.class))).thenReturn(cartItemEntity);
        
        
        
        when(cartItemMapper.toDto(cartItemEntity)).thenReturn(cartItemDTO);
 
        CartItemDTO result = cartService.addItemsToCart(cartId, productId);
 
        assertNotNull(result);
        assertEquals(cartItemDTO, result);
        assertEquals(2, cartItemEntity.getQuantity());
 
        verify(cartItemRepository, times(1)).save(any(CartItemEntity.class));
	}
	
	@Test
	public void addItemsToCart_NewItem_Success_Test() {
		Integer cartId = 1;
        Integer productId = 101;
 
        when(productServiceClient.isValidProduct(productId)).thenReturn(true);
        when(cartRepository.findById(cartId)).thenReturn(Optional.of(cartEntity));
        
        CartItemEntity newCartItemEntity = new CartItemEntity();
        newCartItemEntity.setCartItemId(2);
        newCartItemEntity.setProductId(productId);
        newCartItemEntity.setQuantity(1);
        newCartItemEntity.setCartEntity(cartEntity);
        
        InventoryDTO inventory=new InventoryDTO(1,1,3,0,null);
        
        when(inventoryServiceClient.getInventoryByProductId(productId))
        .thenReturn(inventory);
        
        doNothing().when(inventoryServiceClient).reserveStockForOrder(anyInt(),anyInt());
        
        when(cartItemRepository.save(any(CartItemEntity.class))).thenReturn(newCartItemEntity);
        
        CartItemDTO newCartItemDTO = new CartItemDTO();
        newCartItemDTO.setCartItemId(2);
        newCartItemDTO.setProductId(productId);
        newCartItemDTO.setQuantity(1);
        newCartItemDTO.setCartDTO(cartDTO);
        
        when(cartItemMapper.toDto(newCartItemEntity)).thenReturn(newCartItemDTO);
 
        CartItemDTO result = cartService.addItemsToCart(cartId, productId);
        assertNotNull(result);
        assertEquals(newCartItemDTO, result);
 
        verify(cartItemRepository, times(1)).save(any(CartItemEntity.class));
	}
	
	@Test
	public void addItemsToCart_InvalidProduct_Test() {
		when(productServiceClient.isValidProduct(anyInt())).thenReturn(false);
		assertThatThrownBy(()->cartService.addItemsToCart(1, 22))
		.isInstanceOf(InvalidProductException.class)
		.hasMessage(AppConstants.PRODUCT_NOTFOUND+22);
		
		verify(cartItemRepository,never()).save(any(CartItemEntity.class));
	}
	
	@Test
	public void addItemsToCart_CartNotFound_Test() {
		when(productServiceClient.isValidProduct(anyInt())).thenReturn(true);
		when(cartRepository.findById(anyInt())).thenReturn(Optional.empty());
		
		assertThatThrownBy(()->cartService.addItemsToCart(22,1))
		.isInstanceOf(CartNotFoundException.class)
		.hasMessage(AppConstants.CART_NOTFOUND+22);
		
		verify(cartItemRepository,never()).save(any(CartItemEntity.class));
	}
	
	@Test
    public void testAddItemsToCart_ThrowsInsufficientStockException() {
    
        when(productServiceClient.isValidProduct(1)).thenReturn(true);
        when(cartRepository.findById(anyInt())).thenReturn(Optional.of(cartEntity));
 
        
        InventoryDTO mockInventory = new InventoryDTO();
        mockInventory.setAvailableStock(0);
        when(inventoryServiceClient.getInventoryByProductId(1)).thenReturn(mockInventory);
 
        assertThrows(InsufficientStockException.class, () -> {
            cartService.addItemsToCart(1, 1);
        });
 
        
        verify(inventoryServiceClient, never()).reserveStockForOrder(anyInt(), anyInt());
        verify(cartItemRepository,never()).save(any(CartItemEntity.class));
    }
	
	@Test
    public void testFallbackForProductValidation_ThrowsProductServiceDownException() {
      
        Integer cartId = 1;
        Integer productId = 100;
        Throwable mockThrowable = new RuntimeException("Product service failure");
 
        assertThatThrownBy(()->cartService.fallbackForProductValidation(cartId, productId, mockThrowable))
        .isInstanceOf(ProductServiceDownException.class)
        .hasMessage(AppConstants.PRODUCT_SERVICE_DOWN);
    }
	
	@Test
	public void removeItemsFromCart_CartNotFound_Test() {
		when(cartRepository.findById(anyInt())).thenReturn(Optional.empty());
		
		assertThatThrownBy(()->cartService.removeItemsFromCart(22,1))
		.isInstanceOf(CartNotFoundException.class)
		.hasMessage(AppConstants.CART_NOTFOUND+22);
		
		verify(cartItemRepository,never()).save(any(CartItemEntity.class));
		
	}
	
	
	
	@Test
	public void removeItemsFromCart_InvalidProduct_Test() {
		when(cartRepository.findById(anyInt())).thenReturn(Optional.of(cartEntity));
		
		assertThatThrownBy(()->cartService.removeItemsFromCart(1,22))
		.isInstanceOf(InvalidProductException.class)
		.hasMessage(AppConstants.PRODUCT_NOTFOUND+22);
		
		verify(cartItemRepository,never()).save(any(CartItemEntity.class));
	}
	
	@Test
	public void removeItemsFromCart_Success_Test() {
		when(cartRepository.findById(anyInt())).thenReturn(Optional.of(cartEntity));		
		when(cartItemMapper.toDto(cartItemEntity)).thenReturn(cartItemDTO);
		
		CartItemDTO result = cartService.removeItemsFromCart(1,1);
		
		assertNotNull(result);
	    assertEquals(cartItemDTO, result);
 
	    verify(cartItemRepository, times(1)).deleteAll(anyList());
	    verify(cartRepository, times(1)).save(cartEntity);
	}
	
	@Test
	public void modifyCart_CartNotFound_Test() {
		when(cartRepository.findById(anyInt())).thenReturn(Optional.empty());
		assertThatThrownBy(()->cartService.modifyQuantity(23, 1, 3))
		.isInstanceOf(CartNotFoundException.class)
		.hasMessage(AppConstants.CART_NOTFOUND+23);
		
		verify(cartItemRepository, never()).save(any(CartItemEntity.class));
	}
	
	@Test
	public void modifyCart_InvalidProduct_Test() {
		when(cartRepository.findById(anyInt())).thenReturn(Optional.of(cartEntity));
		assertThatThrownBy(()->cartService.modifyQuantity(1, 22, 3))
		.isInstanceOf(InvalidProductException.class)
		.hasMessage(AppConstants.PRODUCT_NOTFOUND+22);
		
		verify(cartItemRepository, never()).save(any(CartItemEntity.class));
	}
	
	@Test
	public void modifyCart_Success_Test() {
		Integer cartId=1,productId=1,quantity=5;
		when(cartRepository.findById(cartId)).thenReturn(Optional.of(cartEntity));
		cartItemEntity.setQuantity(quantity);
		
		InventoryDTO inventory=new InventoryDTO(1,1,10,5,null);
		
		when(inventoryServiceClient.getInventoryByProductId(productId))
		.thenReturn(inventory);
		when(cartItemRepository.save(cartItemEntity)).thenReturn(cartItemEntity);
		cartItemDTO.setQuantity(quantity);
		when(cartItemMapper.toDto(cartItemEntity)).thenReturn(cartItemDTO);
		
		CartItemDTO result=cartService.modifyQuantity(cartId, productId, quantity);
		
		assertNotNull(result);
		assertEquals(quantity,result.getQuantity() );
		assertEquals(result.getProductId(),productId);
		
		verify(cartItemRepository,times(1)).save(cartItemEntity);
	}
	
	@Test
    public void testModifyQuantity_ThrowsInsufficientStockException() {
        
        Integer cartId = 1;
        Integer productId = 1;
        Integer requestedQuantity = 5;
 
        
        InventoryDTO mockInventory = new InventoryDTO();
        mockInventory.setAvailableStock(3);
 
        
        when(cartRepository.findById(cartId)).thenReturn(Optional.of(cartEntity));
        when(inventoryServiceClient.getInventoryByProductId(productId)).thenReturn(mockInventory);
 
       
        assertThrows(InsufficientStockException.class, () -> {
            cartService.modifyQuantity(cartId, productId, requestedQuantity);
        });
 
        // Verify log output and interactions
        verify(cartRepository, times(1)).findById(cartId);
        verify(inventoryServiceClient, times(1)).getInventoryByProductId(productId);
        verify(inventoryServiceClient, never()).reserveStockForOrder(anyInt(), anyInt()); // Should not reserve stock if exception is thrown
        verify(inventoryServiceClient, never()).releaseReservedStock(anyInt(), anyInt()); // Should not release stock if exception is thrown
       
    }
	
	@Test
	public void clearCart_CartNotFound_Test() {
		when(cartRepository.findById(anyInt())).thenReturn(Optional.empty());
		assertThatThrownBy(()->cartService.clearCart(1))
		.isInstanceOf(CartNotFoundException.class)
		.hasMessage(AppConstants.CART_NOTFOUND+1);
		
		verify(cartRepository,never()).save(any(CartEntity.class));
	}
	
	@Test
	public void clearCart_Success_Test() {
		when(cartRepository.findById(anyInt())).thenReturn(Optional.of(cartEntity));
		doNothing().when(inventoryServiceClient).releaseReservedStock(anyInt(), anyInt());
		
		assertEquals(AppConstants.CLEAR_CART,cartService.clearCart(1));
		
		verify(cartRepository,times(1)).save(any(CartEntity.class));
	}
	
	@Test
	public void getCartByCustomerId_CustomerNotFound_Test() {
		when(cartRepository.findByCustomerId(anyInt())).thenReturn(Optional.empty());
		assertThatThrownBy(()->cartService.getCartByCustomerId(24))
		.isInstanceOf(CartNotFoundException.class)
		.hasMessage(AppConstants.CART_NOTFOUND_FOR_CUSTOMER+24);
		
		verify(cartRepository).findByCustomerId(24);
	}
	
	@Test
	public void getCartByCustomerId_Success_Test() {
		when(cartRepository.findByCustomerId(1)).thenReturn(Optional.of(cartEntity));
		when(cartMapper.toDto(cartEntity)).thenReturn(cartDTO);
		
		CartDTO result=cartService.getCartByCustomerId(1);
		
		assertNotNull(result);
		assertEquals(result.getCartId(), cartDTO.getCartId());
		assertEquals(result.getCustomerId(), cartDTO.getCustomerId());
		
		verify(cartRepository,times(1)).findByCustomerId(1);
	}
	
	@Test
	public void getCartByCartId_CartNotFound_Test() {
		when(cartRepository.findById(anyInt())).thenReturn(Optional.empty());
		assertThatThrownBy(()->cartService.getCartById(24))
		.isInstanceOf(CartNotFoundException.class)
		.hasMessage(AppConstants.CART_NOTFOUND+24);
		
		verify(cartRepository).findById(24);
	}
	
	@Test
	public void getCartByCartId_Success_Test() {
		when(cartRepository.findById(1)).thenReturn(Optional.of(cartEntity));
		when(cartMapper.toDto(cartEntity)).thenReturn(cartDTO);
		
		CartDTO result=cartService.getCartById(1);
		
		assertNotNull(result);
		assertEquals(result.getCartId(), cartDTO.getCartId());
		assertEquals(result.getCustomerId(), cartDTO.getCustomerId());
		
		verify(cartRepository,times(1)).findById(1);
	}
}
 