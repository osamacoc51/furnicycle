package com.furnicycle.orderservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.furnicycle.orderservice.dto.OrderDTO;
import com.furnicycle.orderservice.entity.OrderEntity;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    OrderDTO toOrderDTO(OrderEntity orderEntity);

    OrderEntity toOrderEntity(OrderDTO orderDTO);
}
