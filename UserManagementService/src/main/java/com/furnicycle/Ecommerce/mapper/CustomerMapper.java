package com.furnicycle.Ecommerce.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.furnicycle.Ecommerce.DTO.CustomerDTO;
import com.furnicycle.Ecommerce.Entity.CustomerEntity;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface CustomerMapper {
	CustomerMapper INSTANCE=Mappers.getMapper(CustomerMapper.class);
	
	@Mapping(source = "userEntity" , target="userDTO")
	CustomerDTO toDTO(CustomerEntity customerEntity);
	
	@Mapping(source="userDTO", target="userEntity")
	CustomerEntity toEntity(CustomerDTO customerDTO);
}
