package com.furnicycle.Ecommerce.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name="cartitem")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartItemEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer cartItemId;
	
	private Integer quantity;
	
	private Integer productId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="cartId")
	@JsonIgnore
	private CartEntity cartEntity;


}
