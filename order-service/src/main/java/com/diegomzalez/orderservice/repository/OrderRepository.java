package com.diegomzalez.orderservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.diegomzalez.orderservice.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
