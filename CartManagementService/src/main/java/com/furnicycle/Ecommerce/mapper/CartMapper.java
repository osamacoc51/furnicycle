package com.furnicycle.Ecommerce.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.furnicycle.Ecommerce.dto.CartDTO;
import com.furnicycle.Ecommerce.entity.CartEntity;

@Mapper(componentModel = "spring",uses = CartItemMapper.class)
public interface CartMapper {

  //  CartMapper INSTANCE = Mappers.getMapper(CartMapper.class);

    @Mapping(target = "cartItemDtos", source = "cartItemEntities")
    CartDTO toDto(CartEntity cartEntity);

    @Mapping(target = "cartItemEntities", source = "cartItemDtos")
    CartEntity toEntity(CartDTO cartDto);
}