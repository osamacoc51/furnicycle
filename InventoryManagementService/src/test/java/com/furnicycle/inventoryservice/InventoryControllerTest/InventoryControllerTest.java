package com.furnicycle.inventoryservice.InventoryControllerTest;


import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Date;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.furnicycle.inventoryservice.constants.AppConstants;
import com.furnicycle.inventoryservice.controller.InventoryController;
import com.furnicycle.inventoryservice.dto.InventoryDTO;
import com.furnicycle.inventoryservice.exception.InsufficientStockException;
import com.furnicycle.inventoryservice.exception.InventoryNotFoundException;
import com.furnicycle.inventoryservice.service.InventoryService;

@WebMvcTest(InventoryController.class)
public class InventoryControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InventoryService inventoryService;

    InventoryDTO inventoryDTO;

    @BeforeEach
    public void setup() {
        inventoryDTO = new InventoryDTO();
        inventoryDTO.setInventoryId(1);
        inventoryDTO.setAvailableStock(5);
        inventoryDTO.setReservedStock(0);
        inventoryDTO.setProductId(1);
    }

    @AfterEach
    public void teardown() {
        inventoryDTO = null;
    }

    @Test
    public void addStock_Test() throws JsonProcessingException, Exception {
        when(inventoryService.addStock(anyInt(), anyInt())).thenReturn(inventoryDTO);

        mockMvc.perform(post("/inventory/" + 1 + "/add?quantity=5"))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.inventoryId").value(inventoryDTO.getInventoryId()))
                .andExpect(jsonPath("$.productId").value(inventoryDTO.getProductId()))
                .andExpect(jsonPath("$.availableStock").value(inventoryDTO.getAvailableStock()))
                .andExpect(jsonPath("$.reservedStock").value(inventoryDTO.getReservedStock()));
    }

    @Test
    public void increaseAvailableStockAfterOrderCancel() throws JsonProcessingException, Exception {
        Integer productId = 1;
        Integer quantity = 2;
        inventoryDTO.setAvailableStock(inventoryDTO.getAvailableStock() + quantity);

        when(inventoryService.increaseAvailableStockAfterOrderCancel(productId, quantity))
                .thenReturn(inventoryDTO);

        mockMvc.perform(put("/inventory/increaseAvailableStock?productId=1&quantity=2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inventoryDTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.inventoryId").value(inventoryDTO.getInventoryId()))
                .andExpect(jsonPath("$.productId").value(inventoryDTO.getProductId()))
                .andExpect(jsonPath("$.availableStock").value(inventoryDTO.getAvailableStock()))
                .andExpect(jsonPath("$.reservedStock").value(inventoryDTO.getReservedStock()));
    }

    @Test
    public void removeStock() throws JsonProcessingException, Exception {
        Integer quantity = 2;
        Integer productId = 1;
        inventoryDTO.setAvailableStock(inventoryDTO.getAvailableStock() - quantity);

        when(inventoryService.removeStock(anyInt(), anyInt())).thenReturn(inventoryDTO);

        mockMvc.perform(post("/inventory/{productId}/remove?quantity={quantity}", productId, quantity)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inventoryDTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.inventoryId").value(inventoryDTO.getInventoryId()))
                .andExpect(jsonPath("$.productId").value(inventoryDTO.getProductId()))
                .andExpect(jsonPath("$.availableStock").value(inventoryDTO.getAvailableStock()))
                .andExpect(jsonPath("$.reservedStock").value(inventoryDTO.getReservedStock()));
    }

    @Test
    public void removeStock_InventoryNotFound() throws JsonProcessingException, Exception {
        Integer quantity = 2;
        Integer productId = 1;

        when(inventoryService.removeStock(anyInt(), anyInt()))
                .thenThrow(new InventoryNotFoundException(AppConstants.INVENTORY_NOTFOUND + productId));

        mockMvc.perform(post("/inventory/{productId}/remove?quantity={quantity}", productId, quantity))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(AppConstants.INVENTORY_NOTFOUND + productId));
    }

    @Test
    public void removeStock_InsufficientStock() throws JsonProcessingException, Exception {
        Integer quantity = 2;
        Integer productId = 1;

        when(inventoryService.removeStock(anyInt(), anyInt()))
                .thenThrow(new InsufficientStockException(AppConstants.STOCK_NA));

        mockMvc.perform(post("/inventory/{productId}/remove?quantity={quantity}", productId, quantity))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value(AppConstants.STOCK_NA));
    }

    @Test
    public void reserveStockForOrder_InventoryNotFound() throws JsonProcessingException, Exception {
        Integer quantity = 2;
        Integer productId = 1;

        when(inventoryService.reserveStockForOrder(productId, quantity))
                .thenThrow(new InventoryNotFoundException(AppConstants.INVENTORY_NOTFOUND + productId));

        mockMvc.perform(post("/inventory/{productId}/reserve?quantity={quantity}", productId, quantity))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(AppConstants.INVENTORY_NOTFOUND + productId));
    }

    @Test
    public void reserveStockForOrder_Success_Test() throws JsonProcessingException, Exception {
        InventoryDTO in = new InventoryDTO(2, 2, 5, 3, new Date());

        when(inventoryService.reserveStockForOrder(2, 2)).thenReturn(true);

        in.setReservedStock(in.getReservedStock() - 2);

        mockMvc.perform(post("/inventory/{productId}/reserve?quantity={quantity}", 2, 2))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(AppConstants.STOCK_RESERVED));
    }

    @Test
    public void reserveStockForOrder_StockInsufficient_Test() throws JsonProcessingException, Exception {
        when(inventoryService.reserveStockForOrder(2, 5)).thenReturn(false);

        mockMvc.perform(post("/inventory/{productId}/reserve?quantity={quantity}", 2, 5))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string(AppConstants.STOCK_INSUFFICIENT));
    }

    @Test
    public void releaseReservedStock_InventoryNotFound() throws JsonProcessingException, Exception {
        Integer quantity = 2;
        Integer productId = 1;

        when(inventoryService.releaseReservedStock(productId, quantity))
                .thenThrow(new InventoryNotFoundException(AppConstants.INVENTORY_NOTFOUND + productId));

        mockMvc.perform(post("/inventory/{productId}/release?quantity={quantity}", productId, quantity))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(AppConstants.INVENTORY_NOTFOUND + productId));
    }

    @Test
    public void releaseReservedStock_NotRelease_Test() throws JsonProcessingException, Exception {
        when(inventoryService.releaseReservedStock(2, 5)).thenReturn(false);

        mockMvc.perform(post("/inventory/{productId}/release?quantity={quantity}", 2, 5))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string(AppConstants.RESERVED_STOCK_NOTRELEASED));
    }

    @Test
    public void releaseReservedStock_Release_Test() throws JsonProcessingException, Exception {
        when(inventoryService.releaseReservedStock(2, 5)).thenReturn(true);

        mockMvc.perform(post("/inventory/{productId}/release?quantity={quantity}", 2, 5))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(AppConstants.RESERVED_STOCK_RELEASED));
    }

    @Test
    public void reduceReservedStockAfterOrder_InventoryNotFound() throws JsonProcessingException, Exception {
        Integer quantity = 2;
        Integer productId = 1;

        when(inventoryService.reduceReservedStockAfterOrder(productId, quantity))
                .thenThrow(new InventoryNotFoundException(AppConstants.INVENTORY_NOTFOUND + productId));

        mockMvc.perform(post("/inventory/{productId}/reduceReserved?quantity={quantity}", productId, quantity))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(AppConstants.INVENTORY_NOTFOUND + productId));
    }
}
