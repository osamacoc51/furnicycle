package com.furnicycle.inventoryservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.furnicycle.inventoryservice.dto.InventoryDTO;
import com.furnicycle.inventoryservice.entity.InventoryEntity;

@Mapper(componentModel = "spring")
public interface InventoryMapper {

	InventoryMapper INSTANCE=Mappers.getMapper(InventoryMapper.class);
	
	InventoryDTO toDTO(InventoryEntity inventoryEntity);
	
	InventoryEntity toEntity(InventoryDTO inventoryDTO);
}
