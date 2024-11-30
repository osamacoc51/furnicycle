package com.furnicycle.Ecommerce.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.furnicycle.Ecommerce.dto.CartItemDTO;
import com.furnicycle.Ecommerce.entity.CartItemEntity;

@Mapper(componentModel = "spring")
public interface CartItemMapper {
    
    //CartItemMapper INSTANCE = Mappers.getMapper(CartItemMapper.class);

    @Mapping(target = "cartDTO", source = "cartEntity")
    CartItemDTO toDto(CartItemEntity cartItemEntity);

    @Mapping(target = "cartEntity", source = "cartDTO")
    CartItemEntity toEntity(CartItemDTO cartItemDto);
}