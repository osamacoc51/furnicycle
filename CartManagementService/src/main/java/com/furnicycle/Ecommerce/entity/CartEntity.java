package com.furnicycle.Ecommerce.entity;


import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
@Table(name="cart")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartEntity {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer cartId;
    private Date creationDate;
    
    private Integer customerId;
    
    @OneToMany(mappedBy = "cartEntity",cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<CartItemEntity> cartItemEntities;

}
