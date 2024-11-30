package com.furnicycle.orderservice.dto;

import java.util.Date;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer orderId;

    private Integer customerId;  // Reference to CustomerEntity from user-service

    private Date orderDate;

    private String orderStatus;  // e.g., PENDING, SHIPPED, DELIVERED, CANCELED

    private Double totalAmount;

    private Integer cartId;
}
