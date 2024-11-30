package com.furnicycle.Ecommerce.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="category")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer categoryId;
	
	private String categoryName;
	
	private String categoryDescription;
	
	@OneToMany(mappedBy = "categoryEntity",cascade = CascadeType.ALL,orphanRemoval = true)
    @JsonManagedReference
	@JsonIgnore
	private List<ProductEntity> productsEntity;

	public void addPrdouct(ProductEntity product) {
		this.productsEntity.add(product);
		product.setCategoryEntity(this);
		
	}
//	
	public void removeProduct(ProductEntity product) {
	    this.productsEntity.remove(product);
	    product.setCategoryEntity(null);
	}
}
