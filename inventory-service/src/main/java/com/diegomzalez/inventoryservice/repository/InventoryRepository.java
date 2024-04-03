package com.diegomzalez.iventoryservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.diegomzalez.iventoryservice.model.Inventory;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    public Optional<Inventory> findBySkuCode(String skuCode);
}
