package com.furnicycle.Ecommerce.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.furnicycle.Ecommerce.DTO.UserDTO;
import com.furnicycle.Ecommerce.Entity.UserEntity;

@Mapper(componentModel = "spring")
public interface UserMapper {
	UserMapper INSTANCE=Mappers.getMapper(UserMapper.class);
	
	UserDTO toDTO(UserEntity userEntity);
	
	UserEntity toEntity(UserDTO userDTO);

}
