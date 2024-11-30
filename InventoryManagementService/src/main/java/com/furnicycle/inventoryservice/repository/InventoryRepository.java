package com.furnicycle.inventoryservice.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.furnicycle.inventoryservice.entity.InventoryEntity;

@Repository
public interface InventoryRepository extends JpaRepository<InventoryEntity, Integer>{
	Optional<InventoryEntity> findByProductId(Integer productId);
}
