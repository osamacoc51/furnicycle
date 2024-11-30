package com.furnicycle.Ecommerce.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.furnicycle.Ecommerce.dto.CategoryDTO;
import com.furnicycle.Ecommerce.entity.CategoryEntity;

@Mapper(componentModel = "spring" /*,uses=ProductMapper.class*/)
public interface CategoryMapper {
 //   CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

    @Mapping(source = "productsEntity", target = "productsDTO")
    CategoryDTO toDTO(CategoryEntity categoryEntity );

    @Mapping(source = "productsDTO", target = "productsEntity")
    CategoryEntity toEntity(CategoryDTO categoryDTO);
}
