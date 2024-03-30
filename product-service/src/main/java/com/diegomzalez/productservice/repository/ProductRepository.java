package com.diegomzalez.productservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.diegomzalez.productservice.model.Product;

public interface ProductRepository extends MongoRepository<Product, String> {

}
