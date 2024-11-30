package com.furnicycle.Ecommerce.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
//import org.mapstruct.factory.Mappers;

import com.furnicycle.Ecommerce.dto.CategoryDTO;
import com.furnicycle.Ecommerce.dto.ProductDTO;
import com.furnicycle.Ecommerce.entity.CategoryEntity;
import com.furnicycle.Ecommerce.entity.ProductEntity;

@Mapper(componentModel = "spring"/*,uses=CategoryMapper.class*/)
public interface ProductMapper {
	
	//ProductMapper INSTANCE=Mappers.getMapper(ProductMapper.class);
	
	@Mapping(source="categoryEntity", target="categoryDTO", 
			qualifiedByName = "toCategoryDTOWithoutProducts")
	ProductDTO toDTO(ProductEntity productEntity);
	
	@Mapping(source="categoryDTO", target="categoryEntity", 
			qualifiedByName = "toCategoryEntityWithoutProducts")
	ProductEntity toEntity(ProductDTO productDTO);
	
	@Named("toCategoryDTOWithoutProducts")
    default CategoryDTO toCategoryDTOWithoutProducts(CategoryEntity categoryEntity) {
        if (categoryEntity == null) {
            return null;
        }
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setCategoryId(categoryEntity.getCategoryId());
        categoryDTO.setCategoryName(categoryEntity.getCategoryName());
        categoryDTO.setCategoryDescription(categoryEntity.getCategoryDescription());
        return categoryDTO;
    }

    @Named("toCategoryEntityWithoutProducts")
    default CategoryEntity toCategoryEntityWithoutProducts(CategoryDTO categoryDTO) {
        if (categoryDTO == null) {
            return null;
        }
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setCategoryId(categoryDTO.getCategoryId());
        categoryEntity.setCategoryName(categoryDTO.getCategoryName());
        categoryEntity.setCategoryDescription(categoryDTO.getCategoryDescription());
        return categoryEntity;
    }
}

